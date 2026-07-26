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

// Anotações Lombok comentadas (desativadas) — sobrou do código, sem efeito nenhum hoje.
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name = "produto")
// ATENÇÃO: tem um TAB escondido no meio de "seq\t_produto" (entre "seq" e "_produto").
// Isso pode fazer o nome da sequence no banco não bater com o esperado — vale checar
// se não é só um espaço bugado ao copiar/colar o código.
@SequenceGenerator(name = "seq_produto", sequenceName = "seq	_produto", allocationSize = 1, initialValue = 1)
public class ProdutoJL {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto")
	private long id; // primitivo "long" (não "Long") — não pode ser nulo, sempre tem valor default 0

	@NotBlank(message = "Nome não pode ser null ou vazio")
	@Column(unique = true)
	private String nome;

	// @Min(5) + @Positive juntos é redundante: se o valor mínimo já é 5,
	// automaticamente já é positivo (> 0). @Positive não agrega nada aqui.
	@Min(value = 5, message = "Valor minimo deve ser 5")
	@Positive(message = "Valor do produto deve ser maioo que zero")
	private Double preco;

	// Nome da validação é enganoso: @Min(5) na QUANTIDADE do produto em estoque
	// significa que TODO produto precisa ter no mínimo 5 unidades cadastradas —
	// vale confirmar se essa regra é intencional (parece mais fazer sentido no
	// preço).
	@Min(value = 5, message = "Valor minimo de quantidade deve ser 5")
	private int quantidade;

	/* Muitos produto para uma categoria */
	// Lado N:1 da relação com Categoria1 (visto anteriormente).
	@NotNull(message = "Categoria deve ser informada")
	@JoinColumn(name = "categoria1_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "categoria_fk"))
	// Igual à Endereco: @JsonIgnoreProperties sem parâmetros não tem efeito real.
	@JsonIgnoreProperties
	// EAGER = a categoria é carregada JUNTO com o produto sempre (diferente do LAZY
	// usado na lista de produtos dentro de Categoria1) — faz sentido aqui, já que
	// categoria é um dado "leve" e normalmente necessário junto com o produto.
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