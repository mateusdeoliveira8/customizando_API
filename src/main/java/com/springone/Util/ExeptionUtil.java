package com.springone.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

// Classe utilitária (nome com pequeno erro de digitação: "ExeptionUtil" ao invés
// de "ExceptionUtil" — falta o "c"). Sua responsabilidade é traduzir exceções
// técnicas do banco de dados (PostgreSQL) em mensagens amigáveis para o usuário final,
// em vez de expor erros crus de SQL na tela/API.
public class ExeptionUtil {

	// Pattern (expressão regular) compilado UMA ÚNICA VEZ como constante estática,
	// para não recompilar a regex toda vez que o método for chamado (otimização).
	// Essa regex captura o formato típico de erro de chave duplicada do PostgreSQL,
	// tipo:
	// "Key (cpf)=(12345678900) already exists."
	// Grupo 1 -> nome da coluna (ex: "cpf")
	// Grupo 2 -> valor duplicado (ex: "12345678900")
	// "\\(" e "\\)" -> escapam os parênteses, pois em regex eles têm significado
	// especial
	// ".*?" -> captura "qualquer coisa", de forma "preguiçosa" (non-greedy, pega o
	// menor trecho possível)
	private static final Pattern DUPLICATE_KEY_PATTERN = Pattern.compile("Key \\((.*?)\\)=\\((.*?)\\)");

	// Método público que recebe uma RuntimeException genérica e tenta extrair
	// uma mensagem de erro mais legível, investigando a "cadeia de causas" da
	// exceção.
	public static String extrairMensagem(RuntimeException ex) {

		// Começa a análise pela própria exceção recebida.
		Throwable causa = ex;

		// Em Java, exceções podem estar "encadeadas": uma exceção pode ter sido
		// causada por outra (ex: uma DataIntegrityViolationException do Spring
		// pode ter sido causada por uma PSQLException do driver do Postgres).
		// Esse loop percorre essa cadeia de causas (getCause()) até encontrar
		// a causa raiz do erro, ou até não haver mais causas (null).
		while (causa != null) {

			// "instanceof PSQLException psqle" é a sintaxe de PATTERN MATCHING
			// introduzida no Java 16: além de checar o tipo, já cria a variável
			// "psqle" já convertida (cast) para PSQLException automaticamente.
			// PSQLException é a exceção específica lançada pelo driver JDBC do PostgreSQL.
			if (causa instanceof PSQLException psqle) {

				// Pega o objeto de mensagem de erro detalhado retornado pelo servidor
				// do PostgreSQL (contém código SQL, detalhe, mensagem, etc.)
				ServerErrorMessage erro = psqle.getServerErrorMessage();

				if (erro != null) {

					// Prioridade 1: usa o campo "Detail" do erro do Postgres, se existir.
					// Geralmente é o campo mais descritivo (ex: "Key (cpf)=(123) already exists.")
					if (erro.getDetail() != null && !erro.getDetail().isBlank()) {
						return formatarMensagemDuplicidade(erro.getDetail());
					}

					// Prioridade 2: se não tiver "Detail", usa a mensagem principal do erro.
					if (erro.getMessage() != null && !erro.getMessage().isBlank()) {
						return formatarMensagemDuplicidade(erro.getMessage());
					}
				}

				// Prioridade 3 (fallback): se "erro" for null ou não tiver detail/message,
				// usa a mensagem genérica da própria exceção PSQLException.
				return formatarMensagemDuplicidade(psqle.getMessage());
			}

			// Se a causa atual não for uma PSQLException, avança para a PRÓXIMA causa
			// na cadeia (a exceção que originou essa), e repete o processo.
			causa = causa.getCause();
		}

		// Se percorreu toda a cadeia de causas e não encontrou nenhuma PSQLException,
		// usa a mensagem da exceção original recebida como parâmetro.
		return formatarMensagemDuplicidade(ex.getMessage());
	}

	// Método muito parecido com "extrairMensagem", mas recebe um Throwable genérico
	// (não só RuntimeException) e tem uma lógica ligeiramente mais simples
	// (não separa "Detail" e "Message" em duas tentativas com validação de isBlank
	// no getMessage(), só verifica se é null).
	//
	// OBSERVAÇÃO: esse método é praticamente DUPLICADO do "extrairMensagem" acima,
	// fazendo quase a mesma coisa com pequenas diferenças. Isso é uma redundância
	// que poderia ser evitada, reaproveitando um método só.
	public static String getMensagemLimpa(Throwable ex) {

		Throwable causa = ex;

		while (causa != null) {

			// Aqui o nome completo da classe é usado (org.postgresql.util.PSQLException)
			// em vez do import já feito no topo do arquivo — funciona igual,
			// mas é redundante, já que PSQLException já está importado.
			if (causa instanceof org.postgresql.util.PSQLException psqle) {

				// "var" infere o tipo automaticamente (nesse caso, ServerErrorMessage).
				// Só funciona a partir do Java 10+.
				var erro = psqle.getServerErrorMessage();

				if (erro != null) {

					// Mensagem mais amigável
					// Só checa se getDetail() é diferente de null (não checa isBlank(),
					// diferente do método anterior — inconsistência entre os dois métodos).
					if (erro.getDetail() != null) {
						return formatarMensagemDuplicidade(erro.getDetail());
					}

					return formatarMensagemDuplicidade(erro.getMessage());
				}
			}

			causa = causa.getCause();
		}

		return formatarMensagemDuplicidade(ex.getMessage());
	}

	// Método PRIVADO (uso interno da classe) responsável por pegar uma mensagem
	// de erro "crua" e transformá-la em algo mais legível para o usuário final,
	// especialmente para o caso de CHAVE DUPLICADA (violação de constraint UNIQUE).
	private static String formatarMensagemDuplicidade(String mensagem) {

		// Se não tiver mensagem nenhuma, retorna um texto genérico padrão.
		if (mensagem == null || mensagem.isBlank()) {
			return "Registro já cadastrado.";
		}

		// Tenta "casar" a mensagem recebida com o padrão regex definido lá em cima
		// (DUPLICATE_KEY_PATTERN), que captura o formato "Key (campo)=(valor)".
		Matcher matcher = DUPLICATE_KEY_PATTERN.matcher(mensagem);

		// find() procura se existe uma ocorrência do padrão em algum lugar da mensagem.
		if (matcher.find()) {

			// group(1) -> primeiro grupo capturado pela regex = nome do campo/coluna
			// group(2) -> segundo grupo capturado = valor duplicado
			String campo = matcher.group(1);
			String valor = matcher.group(2);

			// Troca underscores por espaços, já que nomes de colunas no banco
			// geralmente usam snake_case (ex: "data_nascimento" -> "data nascimento").
			campo = campo.replace("_", " ");

			// Deixa a primeira letra do campo maiúscula, para ficar mais apresentável
			// (ex: "data nascimento" -> "Data nascimento").
			// charAt(0) pega o primeiro caractere, toUpperCase deixa maiúsculo,
			// e substring(1) pega o resto da string a partir do segundo caractere.
			campo = Character.toUpperCase(campo.charAt(0)) + campo.substring(1);

			// Monta a mensagem final formatada e amigável para o usuário, ex:
			// "Já existe um registro com Cpf igual a "12345678900"."
			return String.format("Já existe um registro com %s igual a \"%s\".", campo, valor);
		}

		// Se a mensagem não bater com o padrão de chave duplicada,
		// retorna a mensagem original sem modificações.
		return mensagem;
	}

	// Método que retorna uma mensagem de erro amigável baseada no CÓDIGO SQL
	// (SQLState) do erro do PostgreSQL, em vez de tentar interpretar o texto da
	// mensagem.
	// Essa abordagem é mais confiável que a de regex, já que os códigos SQL são
	// padronizados.
	public static String getMensagemUsuario(Throwable ex) {

		Throwable causa = ex;

		// Mesmo padrão de navegação pela cadeia de causas dos métodos anteriores.
		while (causa != null) {

			if (causa instanceof org.postgresql.util.PSQLException psqle) {

				// SWITCH EXPRESSION (sintaxe moderna do Java 14+, usando "->" em vez de "case:
				// break;").
				// getSQLState() retorna um código padronizado (definido pelo padrão SQL/ANSI)
				// que identifica o TIPO do erro ocorrido no banco, independente do texto da
				// mensagem.
				return switch (psqle.getSQLState()) {
				// "23505" -> violação de constraint UNIQUE (registro duplicado)
				case "23505" -> "Registro já cadastrado.";
				// "23503" -> violação de FOREIGN KEY (tentando deletar/alterar algo
				// que ainda está referenciado por outro registro)
				case "23503" -> "Registro possui dependências.";
				// "23502" -> violação de NOT NULL (campo obrigatório não preenchido)
				case "23502" -> "Campo obrigatório não informado.";
				// Qualquer outro código de erro cai nesse caso genérico.
				default -> "Erro ao processar a operação.";
				};
			}

			causa = causa.getCause();
		}

		// Se a cadeia de causas não tiver nenhuma PSQLException (ou seja, o erro
		// não veio do banco de dados), retorna uma mensagem totalmente genérica.
		return "Erro interno do sistema.";
	}

}