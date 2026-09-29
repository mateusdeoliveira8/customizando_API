package com.springone.dto;

import com.springone.entity.ItemProdutoVenda;
import com.springone.entity.VendaCompra;

public class ProdutoMaisVendidoDTO {

	private long id;
	private String nomeProduto;
	private double quantidadeVendida;

	public ProdutoMaisVendidoDTO() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public double getQuantidadeVendida() {
		return quantidadeVendida;
	}

	public void setQuantidadeVendida(double quantidadeVendida) {
		this.quantidadeVendida = quantidadeVendida;
	}

	public void preechervendaDTO(VendaCompra vendaCompra) {

	}

	public void preecherVendaDTO(ItemProdutoVenda itemProdutoVenda) {

		this.id = itemProdutoVenda.getId();
		this.nomeProduto = itemProdutoVenda.getProdutoJL().getNome();
		this.quantidadeVendida = itemProdutoVenda.getQuantidade();

	}

}
