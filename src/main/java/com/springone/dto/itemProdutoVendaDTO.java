package com.springone.dto;

import com.springone.entity.ItemProdutoVenda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class itemProdutoVendaDTO {

	private Long id;
	private double valor;
	private double desconto;
	private String nomeProduto;

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public itemProdutoVendaDTO(ItemProdutoVenda item) {
		super();
		this.id = item.getId();
		this.valor = item.getValor();
		this.desconto = item.getDesconto();

		if (item.getProdutoJL() != null) {
			this.nomeProduto = item.getProdutoJL().getNome();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

}
