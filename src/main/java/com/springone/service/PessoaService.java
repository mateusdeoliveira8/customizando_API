package com.springone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springone.dto.CadastroDTO;
import com.springone.dto.EnderecoDTO;
import com.springone.entity.Endereco;
import com.springone.entity.Pessoa;
import com.springone.exception.MsgApiException;
import com.springone.repository.PessoaRepository;

// Marca a classe como um "Service" do Spring: camada de regras de negócio.
// O Spring cria e gerencia essa instância automaticamente (bean).
@Service
public class PessoaService {

	@Autowired
	// Injeção de dependência do repositório responsável por acessar
	// a tabela/entidade Pessoa no banco de dados.
	private PessoaRepository pessoaRepository;


	// Método responsável por CADASTRAR uma nova pessoa, com validações antes de
	// salvar.
	public Pessoa salvarCadastro(Pessoa pessoa) {

		// Validação 1: garante que o nome foi informado.
		// isBlank() verifica se a String é vazia OU só contém espaços em branco
		// (diferente de isEmpty(), que só verifica se está totalmente vazia "").
		if (pessoa.getNome() == null || pessoa.getNome().isBlank()) {
			throw new MsgApiException("Nome deve ser informado");
		}

		// Validação 2: verifica se já existe alguém cadastrado com o mesmo CPF.
		// existeCPF() é um método customizado (provavelmente uma @Query) definido
		// na interface PessoaRepository, não é um método padrão do JpaRepository.
		if (pessoaRepository.existeCPF(pessoa.getCpf())) {
			throw new MsgApiException("já existe uma pessoa com esse cpf");
		}

		// Se passou pelas validações, salva a pessoa no banco (INSERT).
		return pessoaRepository.save(pessoa);
	}

	// Retorna a lista de TODAS as pessoas cadastradas.
	public List<Pessoa> listarCadastro() {
		return pessoaRepository.findAll();
	}

	// Remove uma pessoa do banco a partir do ID.
	public void deletarCadastro(Long id) {

		// Verifica se o ID existe antes de tentar deletar.
		// Evita erro ao tentar excluir algo que não existe.
		if (!pessoaRepository.existsById(id)) {
			throw new MsgApiException("Cadastro nao encontrado!");
		}

		pessoaRepository.deleteById(id);
	}

	// Atualiza os dados de uma pessoa já existente (atualização PARCIAL:
	// só altera "nome" e "cargo", os demais campos permanecem inalterados).
	public Pessoa atualizarCadastro(Long id, Pessoa novoCadastro) {

		// Busca a pessoa pelo ID. Se não encontrar, retorna null (via orElse(null)).
		Pessoa pessoa = pessoaRepository.findById(id).orElse(null);

		if (pessoa == null) {
			throw new MsgApiException("Cadastro nao encontrado");
		} else {
			pessoa.setNome(novoCadastro.getNome());
			pessoa.setCargo(novoCadastro.getCargo());
		}

		// Salva as alterações (aqui vira um UPDATE, pois o objeto já tem ID).
		return pessoaRepository.save(pessoa);
	}

	// Busca uma pessoa pelo ID.
	public Pessoa buscarPorId(Long id) {

		Pessoa pessoa = pessoaRepository.findById(id).orElse(null);

		// ATENÇÃO — BUG AQUI:
		// Se a pessoa NÃO for encontrada, "pessoa" será null (por causa do orElse(null)
		// acima).
		// Só que a validação abaixo tenta chamar "pessoa.getId()" ANTES de checar se
		// "pessoa" é null.
		// Isso vai lançar um NullPointerException em vez de lançar a MsgApiException
		// esperada,
		// porque você não pode chamar um método (getId()) em cima de uma referência
		// nula.
		// O correto seria checar "if (pessoa == null)", igual foi feito nos outros
		// métodos da classe.
		if (pessoa.getId() == null) {
			throw new MsgApiException("Cadastro nao encontrado");
		} else {
			return pessoa;
		}
	}

	// Busca pessoas pelo nome. Se o nome não for informado, retorna todas.
	public List<Pessoa> buscarPorNome(String nome) {

		// Se o parâmetro "nome" vier nulo ou vazio, não faz sentido filtrar,
		// então retorna a listagem completa.
		if (nome == null || nome.isEmpty()) {
			return pessoaRepository.findAll();
		}

		// Caso contrário, usa um método de busca customizado do repository
		// (provavelmente algo como "LIKE %nome%" numa @Query).
		return pessoaRepository.buscarPorNome(nome);
	}

	// Busca uma única pessoa pelo CPF.
	// Método customizado no repository (nome com "p" minúsculo em "buscaporCPF" —
	// pequena inconsistência de nomenclatura, o ideal seria "buscarPorCPF" para
	// seguir o padrão camelCase usado no resto da classe).
	public Pessoa buscarPorCPF(String cpf) {
		return pessoaRepository.buscaporCPF(cpf);
	}

	// Monta uma lista de DTOs (Data Transfer Objects) representando o cadastro
	// completo de cada pessoa, incluindo seus endereços.
	// DTOs são usados para controlar exatamente quais dados serão
	// expostos/retornados
	// pela API, sem enviar a entidade "crua" do banco (evita expor campos sensíveis
	// ou gerar problemas de serialização com relacionamentos JPA).
	public List<CadastroDTO> listaCadastro() {

		// Lista que vai armazenar o resultado final (um DTO por pessoa).
		List<CadastroDTO> lista = new ArrayList<CadastroDTO>();

		// Percorre todas as pessoas cadastradas (reaproveitando o método
		// listarCadastro()).
		for (Pessoa pessoa : listarCadastro()) {

			// Cria um CadastroDTO a partir da pessoa.
			// Presumivelmente o construtor de CadastroDTO já copia os dados básicos
			// da Pessoa (nome, cargo, cpf, etc.) para dentro do DTO.
			CadastroDTO dto = new CadastroDTO(pessoa);

			// Para cada pessoa, percorre a lista de endereços dela (relacionamento
			// One-to-Many entre Pessoa e Endereco) e converte cada Endereco em um
			// EnderecoDTO.
			for (Endereco endereco : pessoa.getEnderecos()) {

				EnderecoDTO enderecoDTO = new EnderecoDTO();
				enderecoDTO.setBairro(endereco.getBairro());
				enderecoDTO.setCidade(endereco.getCidade());
				enderecoDTO.setNumero(endereco.getNumero());
				enderecoDTO.setRua(endereco.getRua());
				enderecoDTO.setUf(endereco.getUf());

				// Adiciona o EnderecoDTO montado dentro da lista de endereços do CadastroDTO.
				dto.getEnderecoDTOs().add(enderecoDTO);
			}

			// Adiciona o CadastroDTO (já com seus endereços) na lista final.
			lista.add(dto);
		}

		return lista;
	}

	public Page<Pessoa> listaPaginada(int page, int size) {
		return pessoaRepository.findAll(PageRequest.of(page, size, Sort.by("nome")));

	}


}