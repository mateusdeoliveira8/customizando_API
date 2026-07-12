package com.springone.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

public class ExeptionUtil {

	private static final Pattern DUPLICATE_KEY_PATTERN = Pattern.compile("Key \\((.*?)\\)=\\((.*?)\\)");

	public static String extrairMensagem(RuntimeException ex) {

		Throwable causa = ex;

		while (causa != null) {

			if (causa instanceof PSQLException psqle) {

				ServerErrorMessage erro = psqle.getServerErrorMessage();

				if (erro != null) {

					if (erro.getDetail() != null && !erro.getDetail().isBlank()) {
						return formatarMensagemDuplicidade(erro.getDetail());
					}

					if (erro.getMessage() != null && !erro.getMessage().isBlank()) {
						return formatarMensagemDuplicidade(erro.getMessage());
					}
				}

				return formatarMensagemDuplicidade(psqle.getMessage());
			}

			causa = causa.getCause();
		}

		return formatarMensagemDuplicidade(ex.getMessage());
	}

	public static String getMensagemLimpa(Throwable ex) {

		Throwable causa = ex;

		while (causa != null) {

			if (causa instanceof org.postgresql.util.PSQLException psqle) {

				var erro = psqle.getServerErrorMessage();

				if (erro != null) {

					// Mensagem mais amigável
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

	private static String formatarMensagemDuplicidade(String mensagem) {

		if (mensagem == null || mensagem.isBlank()) {
			return "Registro já cadastrado.";
		}

		Matcher matcher = DUPLICATE_KEY_PATTERN.matcher(mensagem);

		if (matcher.find()) {

			String campo = matcher.group(1);
			String valor = matcher.group(2);

			campo = campo.replace("_", " ");

			campo = Character.toUpperCase(campo.charAt(0)) + campo.substring(1);

			return String.format("Já existe um registro com %s igual a \"%s\".", campo, valor);
		}

		return mensagem;
	}

	public static String getMensagemUsuario(Throwable ex) {

		Throwable causa = ex;

		while (causa != null) {

			if (causa instanceof org.postgresql.util.PSQLException psqle) {

				return switch (psqle.getSQLState()) {
				case "23505" -> "Registro já cadastrado.";
				case "23503" -> "Registro possui dependências.";
				case "23502" -> "Campo obrigatório não informado.";
				default -> "Erro ao processar a operação.";
				};
			}

			causa = causa.getCause();
		}

		return "Erro interno do sistema.";
	}

}
