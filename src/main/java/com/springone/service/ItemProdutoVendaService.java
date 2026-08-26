package com.springone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
