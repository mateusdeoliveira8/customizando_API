package com.springone.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@NotBlank(message = "O nome deve ser informado")

	private String nome;

	private String idade; // String em vez de int — evita erro de parsing, mas não valida se é número

	@NotBlank(message = "O CPF deve ser informado")
	@Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
	private String cpf;


	@NotBlank(message = "O cargo deve ser informado")
	private String cargo;

	// 1 Pessoa -> N Endereços.
	// cascade = ALL -> qualquer operação na Pessoa (salvar, atualizar, DELETAR)
	// se propaga para os endereços vinculados.
	// orphanRemoval = true -> se um endereço for removido da lista (não só da
	// pessoa,
	// mas se a pessoa inteira for deletada), ele é DELETADO do banco também
	// (diferente da Categoria1, que tinha orphanRemoval = false).
	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)

	// @JsonIgnore -> essa lista nunca aparece quando a Pessoa é convertida para
	// JSON
	// (abordagem mais "bruta" que o @JsonBackReference usado na Categoria1,
	// que só ignora na serialização do lado de baixo, mantendo a lista acessível
	// via DTOs manuais, como o CadastroDTO que já vimos)
	@JsonIgnore
	private List<Endereco> enderecos;

	public Pessoa() {
	}

	// Getters e setters padrão.
	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

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

}