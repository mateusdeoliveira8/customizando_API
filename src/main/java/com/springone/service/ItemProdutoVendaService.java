package com.springone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springone.entity.ItemProdutoVenda;
import com.springone.repository.ItemProdutoVendaRepository;

@Service
public class ItemProdutoVendaService {

	@Autowired
	ItemProdutoVendaRepository itemProdutoVendaRepository;
	
	public ItemProdutoVenda salvar(ItemProdutoVenda itemProdutoVenda) {
		return itemProdutoVendaRepository.save(itemProdutoVenda);
	}

}
