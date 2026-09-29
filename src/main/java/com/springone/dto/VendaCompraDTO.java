package com.springone.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springone.entity.ItemProdutoVenda;
import com.springone.entity.VendaCompra;

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
public class VendaCompraDTO {

	private Long id;
	private String nomeCliente;
	private List<itemProdutoVendaDTO> itensDtos;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date data;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public VendaCompraDTO() {
		super();
	}

	public VendaCompraDTO(VendaCompra vendaCompra) {
		super();
		this.id = vendaCompra.getId();
		;
		if (vendaCompra.getPessoa() != null) {
			this.nomeCliente = vendaCompra.getPessoa().getNome();
		}

		if (vendaCompra.getItensProdutos() != null) {

			List<itemProdutoVendaDTO> itens = new ArrayList<itemProdutoVendaDTO>();

			for (ItemProdutoVenda item : vendaCompra.getItensProdutos()) {
				itens.add(new itemProdutoVendaDTO(item));
			}

			this.itensDtos = itens;
		}

	}

	public void preencherVenda(VendaCompra vendaCompra) {

		this.id = vendaCompra.getId();
		this.data = vendaCompra.getData();

		if (vendaCompra.getPessoa() != null) {
			this.nomeCliente = vendaCompra.getPessoa().getNome();

		}

	}

	public void preecnherItens(VendaCompra vendaCompra) {

		this.itensDtos = new ArrayList<>();

		for (ItemProdutoVenda item : vendaCompra.getItensProdutos()) {
			this.itensDtos.add(new itemProdutoVendaDTO(item));
		}

	}

	public void buscarPorData02(VendaCompra vendaCompra) {

		this.id = vendaCompra.getId();

		if (vendaCompra.getData() != null) {
			this.data = vendaCompra.getData();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public List<itemProdutoVendaDTO> getItensDtos() {
		return itensDtos;
	}

	public void setItensDtos(List<itemProdutoVendaDTO> itensDtos) {
		this.itensDtos = itensDtos;
	}

}
