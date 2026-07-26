package com.springone.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
// Usa a MESMA sequence ("seq_compravenda") que a entidade ItemProdutoVenda usa!
// Isso significa que os IDs de VendaCompra e ItemProdutoVenda são gerados a partir
// da mesma fonte, "competindo" pelos mesmos números (ex: Venda pega o ID 1,
// próximo Item pega o ID 2, mesmo sendo tabelas diferentes). Não quebra nada
// tecnicamente, mas provavelmente não é intencional — o normal seria cada
// entidade ter sua própria sequence.
@SequenceGenerator(name = "seq_compravenda", sequenceName = "seq_compravenda", allocationSize = 1, initialValue = 1)
public class VendaCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_compravenda")
	private Long id;

	private double totalVendaFinal;
	private double desconto;
	private String observacao;
	private Date data;

	// N:1 -> várias vendas podem pertencer à mesma pessoa (cliente).
	@ManyToOne
	@JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private Pessoa pessoa;

	// 1:N -> uma venda tem vários itens de produto.
	// EAGER = a lista de itens é sempre carregada junto com a venda.
	// Campo sem "private" (visibilidade default) e com "I" maiúsculo em
	// "ItensProdutos" — foge da convenção Java (deveria ser "itensProdutos",
	// minúsculo).
	@OneToMany(mappedBy = "vendaCompra", fetch = FetchType.EAGER)
	List<ItemProdutoVenda> ItensProdutos = new ArrayList<ItemProdutoVenda>();

	// Getters e setters padrão.
	public Long getId() {
		return id;
	}

	public List<ItemProdutoVenda> getItensProdutos() {
		return ItensProdutos;
	}

	public void setItensProdutos(List<ItemProdutoVenda> itensProdutos) {
		ItensProdutos = itensProdutos;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getTotalVendaFinal() {
		return totalVendaFinal;
	}

	public void setTotalVendaFinal(double totalVendaFinal) {
		this.totalVendaFinal = totalVendaFinal;
	}

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	// Recalcula o valor total da venda somando o valor de cada item.
	// Assim como calcularValor() do ItemProdutoVenda, precisa ser chamado
	// manualmente (ex: no Service) — o JPA não faz isso sozinho.
	public void calcularValor() {
		this.totalVendaFinal = 0;
		for (ItemProdutoVenda itemProdutoVenda : ItensProdutos) {
			totalVendaFinal += itemProdutoVenda.getValor();
		}
	}

	// Recalcula o desconto total da venda somando o desconto de cada item.
	public void calcularDesconto() {
		this.desconto = 0;
		for (ItemProdutoVenda itemProdutoVenda : ItensProdutos) {
			desconto += itemProdutoVenda.getDesconto();
		}
	}
}