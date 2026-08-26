package com.springone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springone.entity.ItemProdutoVenda;
import com.springone.service.ItemProdutoVendaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/itemprodutovenda")
public class ItemProdutoVendaController {

	@Autowired
	ItemProdutoVendaService itemprodutovendaservice;

	@PostMapping("/salvar")
	public ResponseEntity<ItemProdutoVenda> salvar(@RequestBody @Valid ItemProdutoVenda itemprodutovenda) {
		ItemProdutoVenda item = itemprodutovendaservice.salvar(itemprodutovenda);
		return ResponseEntity.ok(item);

	}

	@PutMapping("/atualizar")
	public ResponseEntity<ItemProdutoVenda> atualizar(@RequestBody ItemProdutoVenda itemprodutovenda) {
		
		return ResponseEntity.ok(itemprodutovendaservice.atualizar(itemprodutovenda));
	}

	@PutMapping("/deletar/{id}")
	public ResponseEntity<Void> detelar(@PathVariable Long id) {
		itemprodutovendaservice.deletar(id);
		return ResponseEntity.ok().build();
		
	}

	public ResponseEntity<List<ItemProdutoVenda>> listar() {
		return ResponseEntity.ok(itemprodutovendaservice.listar());
	}
}

