package com.springone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springone.dto.ProdutoMaisVendidoDTO;
import com.springone.entity.ItemProdutoVenda;
import com.springone.repository.RelatorioRepository;

@Service
public class RelatorioSerivce {

	@Autowired
	RelatorioRepository relatorioRepository;

	public List<ProdutoMaisVendidoDTO> produtoMaisVendido() {

		// Busca no banco de dados todos os registros de ItemProdutoVenda
		// e coloca esses registros dentro da lista.
		List<ItemProdutoVenda> lista = relatorioRepository.findAll();

		// Cria uma lista vazia que vai receber os DTOs
		// depois que transformarmos os dados da entidade.
		List<ProdutoMaisVendidoDTO> listaDtos = new ArrayList<>();

		// Percorre cada ItemProdutoVenda que veio do banco.
		//
		// A variável "item" representa o registro atual
		// durante cada repetição do for.
		for (ItemProdutoVenda item : lista) {

			// Cria um novo objeto DTO.
			//
			// Esse DTO será usado para transportar somente
			// as informações que queremos mostrar no relatório.
			ProdutoMaisVendidoDTO dto = new ProdutoMaisVendidoDTO();


			// Pega as informações do ItemProdutoVenda
			// e coloca essas informações dentro do DTO.
			//
			// Esse método foi criado por nós dentro do DTO.
			dto.preecherVendaDTO(item);


			// Adiciona o DTO que acabamos de preencher
			// dentro da lista de DTOs.
			listaDtos.add(dto);

		}


		// Depois que o for terminar, devolvemos a lista
		// contendo todos os ProdutoMaisVendidoDTO.
		return listaDtos;

	}

}
