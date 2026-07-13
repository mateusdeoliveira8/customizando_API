package com.springone.entity;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "seq_compravenda", sequenceName = "seq_compravenda", allocationSize = 1, initialValue = 1)

public class ItemProdutoVenda {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_compravenda")
	private Long id;
	private int quantidade;
	private double valor;
	private double desconto;

	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "produto_fk"))
	private ProdutoJL produtoJL;

	@ManyToOne
	@JoinColumn(name = "vendacompra_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "vendacompra_fk"))
	private VendaCompra vendaCompra;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public ProdutoJL getProdutoJL() {
		return produtoJL;
	}

	public void setProdutoJL(ProdutoJL produtoJL) {
		this.produtoJL = produtoJL;
	}

	public VendaCompra getVendaCompra() {
		return vendaCompra;
	}

	public void setVendaCompra(VendaCompra vendaCompra) {
		this.vendaCompra = vendaCompra;
	}

}
