package com.springone.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "Categoria1")
@SequenceGenerator(name = "seq_categoria", sequenceName = "seq_categoria", allocationSize = 1, initialValue = 1)

public class Categoria1 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categoria")
	private Long id;

	@NotNull(message = "Nome da categoria deve ser infromada")
	@NotBlank(message = "Nome da categroia deve ser informada")
	@Column(unique = true)
	private String nome;

	/* Uma Categoria para muitos produtos */
	// @JsonIgnoreProperties
	@JsonBackReference
	@OneToMany(mappedBy = "categoria1", fetch = FetchType.LAZY, orphanRemoval = false, cascade = CascadeType.MERGE)
	private List<ProdutoJL> produtoJLs = new ArrayList<ProdutoJL>();

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

	public List<ProdutoJL> getProdutoJLs() {
		return produtoJLs;
	}

	public void setProdutoJLs(List<ProdutoJL> produtoJLs) {
		this.produtoJLs = produtoJLs;
	}

}
