package com.springone.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name = "produto")
@SequenceGenerator(name = "seq_produto", sequenceName = "seq	_produto", allocationSize = 1, initialValue = 1)
public class ProdutoJL {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto")
	private long id;

	@NotBlank(message = "Nome não pode ser null ou vazio")
	@Column(unique = true)
	private String nome;

	@Min(value = 5, message = "Valor minimo deve ser 5")
	@Positive(message = "Valor do produto deve ser maioo que zero")
	private Double preco;

	@Min(value = 5, message = "Valor minimo de quantidade deve ser 5")
	private int quantidade;

	/* Muitos produto para uma categoria */
	@NotNull(message = "Categoria deve ser informada")
	@JoinColumn(name = "categoria1_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "categoria_fk"))
	@JsonIgnoreProperties
	@ManyToOne(fetch = FetchType.EAGER)
	private Categoria1 categoria1;

	public Categoria1 getCategoria1() {
		return categoria1;
	}

	public void setCategoria1(Categoria1 categoria1) {
		this.categoria1 = categoria1;
	}

	public ProdutoJL() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

}
