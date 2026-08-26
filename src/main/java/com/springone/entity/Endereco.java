package com.springone.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity // Mapeia essa classe para uma tabela do banco.
public class Endereco {

	@Id
	// IDENTITY: o próprio banco gera o ID via auto-incremento da coluna
	// (diferente da Categoria1, que usava uma SEQUENCE explícita).
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Campos simples, sem validação (@NotNull/@NotBlank) — diferente da
	// Categoria1, aqui não há nenhuma restrição de preenchimento obrigatório.

	@NotBlank(message = "A rua deve ser informada")
	private String rua;

	@NotNull(message = "O número deve ser informado")
	private int numero;

	@NotBlank(message = "O bairro deve ser informado")
	private String bairro;

	@NotBlank(message = "A cidade deve ser informada")
	private String cidade;

	@NotBlank(message = "A UF deve ser informada")
	@Size(min = 2, max = 2, message = "A UF deve ter 2 caracteres")
	private String uf;


	// Relação N:1 -> vários Endereços pertencem a UMA Pessoa.
	@ManyToOne
	// Cria a coluna de chave estrangeira "pessoa_id" nessa tabela.
	// nullable = false -> todo endereço OBRIGATORIAMENTE precisa estar
	// vinculado a uma pessoa (não pode existir endereço "solto").
	@JoinColumn(name = "pessoa_id", nullable = false)
	// @JsonIgnoreProperties sem parâmetros não faz nada sozinho — precisa
	// especificar QUAIS propriedades ignorar, ex:
	// @JsonIgnoreProperties("enderecos").
	// Do jeito que está, é código morto (não tem efeito real na serialização).
	@JsonIgnoreProperties
	@NotNull(message = "A pessoa deve ser informada")
	private Pessoa pessoa;

	// Construtor vazio manual (aqui não usou Lombok @NoArgsConstructor como na
	// outra entidade — inconsistência de padrão entre as classes do projeto).
	public Endereco() {
		super();
	}

	// Getters e setters padrão.
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}