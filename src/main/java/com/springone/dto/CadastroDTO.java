package com.springone.dto;

import java.util.ArrayList;
import java.util.List;

import com.springone.entity.Pessoa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO (Data Transfer Object): representa os dados de Pessoa formatados
// especificamente para SAÍDA da API, sem expor a entidade JPA diretamente
// (evita vazar detalhes internos, relacionamentos lazy, etc.)

// @Data gera automaticamente getters, setters, equals(), hashCode() e toString()
// — só que aqui os getters/setters TAMBÉM foram escritos manualmente logo abaixo,
// então tá duplicado: @Data já geraria tudo isso sozinho, os @Getter/@Setter
// explícitos e os métodos manuais são redundantes entre si.
@Data
@AllArgsConstructor // Gera construtor com TODOS os campos como parâmetros.
@NoArgsConstructor // Gera construtor vazio.
@Getter // Redundante: @Data já inclui isso.
@Setter // Redundante: @Data já inclui isso.
public class CadastroDTO {

	private Long id;
	private String nome;
	private String idade;
	private String cpf;
	private String cargo;
	private List<EnderecoDTO> enderecoDTOs = new ArrayList<EnderecoDTO>();

	// Construtor customizado: converte uma Pessoa (entidade) em CadastroDTO,
	// copiando campo a campo. Só não copia "enderecoDTOs" aqui — essa lista
	// é preenchida depois, manualmente, no PessoaService.listaCadastro()
	// (que vimos lá atrás), iterando os Endereco da Pessoa.
	public CadastroDTO(Pessoa cadasto) {
		super();
		this.id = cadasto.getId();
		this.nome = cadasto.getNome();
		this.idade = cadasto.getIdade();
		this.cpf = cadasto.getCpf();
		this.cargo = cadasto.getCargo();
	}

	// Getters e setters manuais (redundantes com @Data/@Getter/@Setter acima).
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIdade() {
		return idade;
	}

	public void setIdade(String idade) {
		this.idade = idade;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public List<EnderecoDTO> getEnderecoDTOs() {
		return enderecoDTOs;
	}

	public void setEnderecoDTOs(List<EnderecoDTO> enderecoDTOs) {
		this.enderecoDTOs = enderecoDTOs;
	}
}