package com.springone.dto;

import java.util.ArrayList;
import java.util.List;

import com.springone.entity.Pessoa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CadastroDTO {

	private Long id;
	private String nome;
	private String idade;
	private String cpf;
	private String cargo;
	private List<EnderecoDTO> enderecoDTOs = new ArrayList<EnderecoDTO>();

	public CadastroDTO(Pessoa cadasto) {
		super();
		this.id = cadasto.getId();
		this.nome = cadasto.getNome();
		this.idade = cadasto.getIdade();
		this.cpf = cadasto.getCpf();
		this.cargo = cadasto.getCargo();
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

	public List<EnderecoDTO> getEnderecoDTOs() {
		return enderecoDTOs;
	}

	public void setEnderecoDTOs(List<EnderecoDTO> enderecoDTOs) {
		this.enderecoDTOs = enderecoDTOs;
	}

}
