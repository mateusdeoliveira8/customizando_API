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
import jakarta.persistence.Transient;

// TABELA ASSOCIATIVA (de junção) entre ProdutoJL e VendaCompra.
// Resolve o relacionamento N:N "um produto pode estar em várias vendas,
// uma venda pode ter vários produtos" transformando-o em duas relações N:1
// (ItemProdutoVenda -> ProdutoJL e ItemProdutoVenda -> VendaCompra).
// Diferente de uma associativa "pura" (só com as 2 FKs), essa é enriquecida:
// carrega dados próprios da associação (quantidade, valor, desconto), que não
// pertencem nem só ao produto nem só à venda, mas à combinação dos dois.
@Entity
@SequenceGenerator(name = "seq_compravenda", sequenceName = "seq_compravenda", allocationSize = 1, initialValue = 1)
public class ItemProdutoVenda {
	// Representa cada LINHA de produto dentro de uma venda
	// (ex: "2x Camiseta, R$ 50, 10% desconto").

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_compravenda")
	private Long id;

	private int quantidade;
	private double valor;
	private double desconto;

	// Lado N:1 da associação com o produto.
	@ManyToOne
	// foreignKey(...) dá um NOME customizado à constraint de FK no banco
	// ("produto_fk"), útil para identificar o erro em logs/mensagens do banco.
	@JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "produto_fk"))
	private ProdutoJL produtoJL;

	// Lado N:1 da associação com a venda.
	@ManyToOne
	@JoinColumn(name = "vendacompra_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "vendacompra_fk"))
	private VendaCompra vendaCompra;

	// Getters e setters padrão.
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

	// @Transient diz ao JPA para IGNORAR esse método na persistência
	// (não vira coluna no banco, é só lógica auxiliar em memória).
	// Calcula o valor final do item: (quantidade x preço do produto) - desconto.
	// Precisa ser chamado manualmente (ex: no Service) antes de salvar.
	@Transient
	public void calcularValor() {
		this.valor = (quantidade * produtoJL.getPreco()) - this.desconto;
	}
}