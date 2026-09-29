package com.springone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springone.dto.itemProdutoVendaDTO;
import com.springone.entity.ItemProdutoVenda;
import com.springone.exception.MsgApiException;
import com.springone.repository.ItemProdutoVendaRepository;

@Service
public class ItemProdutoVendaService {

	@Autowired
	ItemProdutoVendaRepository itemProdutoVendaRepository;

	public ItemProdutoVenda salvar(ItemProdutoVenda itemProdutoVenda) {
		return itemProdutoVendaRepository.save(itemProdutoVenda);
	}

	public ItemProdutoVenda atualizar(ItemProdutoVenda itemprodutovenda) {

		if (itemprodutovenda == null) {

			throw new MsgApiException("Item de Produto deve ser passado para serem atualizado");
		}

		return itemProdutoVendaRepository.save(itemprodutovenda);

	}

	public void deletar(Long id) {

		itemProdutoVendaRepository.deleteById(id);
	}

	public List<ItemProdutoVenda> buscarVendaPorProduto1(String nome) {
		return itemProdutoVendaRepository.buscarVendaPorProduto1(nome);

	}

	public List<ItemProdutoVenda> listar() {
		return itemProdutoVendaRepository.findAll();
	}


	public List<itemProdutoVendaDTO> listaPaginada(int page, int size) {
		
		
		List<ItemProdutoVenda> lista = itemProdutoVendaRepository.findAll(PageRequest.of(page, size, Sort.by("id")))
				.getContent();

		List<itemProdutoVendaDTO> listaDTO = new ArrayList<>();
		
		for (ItemProdutoVenda itemprodutovenda : lista) {

			itemProdutoVendaDTO dto = new itemProdutoVendaDTO(itemprodutovenda);
			listaDTO.add(dto);

		}

		return listaDTO;

	}

}
