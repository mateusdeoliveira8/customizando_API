package com.springone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springone.entity.Categoria1;
import com.springone.service.Categoria1Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categoria")
public class ControllerCategoria {

	@Autowired
	private Categoria1Service categoria1Service;

	@GetMapping("/teste")
	public ResponseEntity<String> teste() {
		return ResponseEntity.ok("tudo ok");
	}

	@PostMapping("/salvar")
	public ResponseEntity<Categoria1> salvar(@RequestBody @Valid Categoria1 categoria1) {
		Categoria1 categoria2 = categoria1Service.salvarcategoria(categoria1);
		return ResponseEntity.ok(categoria2);
	}

	@PostMapping("/salvarCategorias")
	public ResponseEntity<List<Categoria1>> salvarCategorias(@RequestBody List<Categoria1> categoria1s) {
		List<Categoria1> categoriasSalvas = categoria1Service.salvarCategorias(categoria1s);
		return ResponseEntity.ok(categoriasSalvas);
	}

	@GetMapping("/listar")
	public ResponseEntity<List<Categoria1>> listarCategoria() {
		return ResponseEntity.ok(categoria1Service.listarCategorias());
	}

	@PutMapping("/atualizar/{id}")
	public ResponseEntity<Categoria1> atualizar(@PathVariable Long id, @RequestBody Categoria1 categoria1) {

		return ResponseEntity.ok(categoria1Service.atualizarCategoria(id, categoria1));
	}

	@PutMapping("/atualizar2")
	public ResponseEntity<Categoria1> atualizar2(@RequestBody Categoria1 categoria1) {
		return ResponseEntity.ok(categoria1Service.atualizar(categoria1));
	}

	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		categoria1Service.deletar(id);
		return ResponseEntity.ok().build();
	}
}