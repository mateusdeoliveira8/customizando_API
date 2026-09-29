package com.springone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springone.dto.FornecedorDTO;
import com.springone.entity.Fornecedor;
import com.springone.service.FornecedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fornecedor")
public class FornecedorController {

	@Autowired
	FornecedorService fornecedorService;

	@GetMapping("/salvar")
	public ResponseEntity<Fornecedor> salvar(@RequestBody @Valid Fornecedor fornecedor) {
		Fornecedor fornecedor2 = fornecedorService.salvar(fornecedor);
		return ResponseEntity.ok(fornecedor2);

	}

	@PostMapping("/listar")
	public ResponseEntity<List<Fornecedor>> listar(@RequestParam String nome) {

		return ResponseEntity.ok(fornecedorService.listar());

	}

	@PutMapping("/atualizar")
	public ResponseEntity<Fornecedor> atualizar(@RequestBody @Valid @PathVariable Long id, Fornecedor fornecedor) {

		return ResponseEntity.ok(fornecedorService.atualizar(id, fornecedor));

	}

	@GetMapping("/cnpj/{cnpj}")
	public ResponseEntity<FornecedorDTO> consultar(@PathVariable String cnpj) {

		return ResponseEntity.ok(fornecedorService.consultar(cnpj));
	}

}
