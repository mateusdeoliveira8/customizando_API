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
import org.springframework.web.bind.annotation.RestController;

import com.springone.entity.Endereco;
import com.springone.service.EnderecoService;

@RestController
@RequestMapping("/api/endereco")
public class ControllerEndereco {

	@Autowired
	private EnderecoService enderecoService;


	// ==========================
	// TESTE DA API
	// ==========================
	@GetMapping("/teste")
	public ResponseEntity<String> teste() {
		return ResponseEntity.ok("Esta funcionado");
	}

	// ==========================
	// SALVAR NOVO Endereco
	// ==========================
	@PostMapping("/salvar")
	public ResponseEntity<Endereco> salvarEndereco(@RequestBody Endereco Endereco) {
		Endereco Enderecosalvo = enderecoService.salvarEndereco(Endereco);
		return ResponseEntity.ok(Enderecosalvo);
	}



	// ==========================
	// ATUALIZAR Endereco
	// ==========================
	@PutMapping("/atualizar/{id}")
	public Endereco atualizar(@PathVariable Long id, @RequestBody Endereco Endereco) {

		return enderecoService.atualizarEndereco(id, Endereco);
	}

	@GetMapping("/listar")
	public ResponseEntity<List<Endereco>> listarEndereco() {
		return ResponseEntity.ok(enderecoService.listarEndereco());
	}

	@PutMapping("/buscarporId/{id}")
	public ResponseEntity<Endereco> buscarPorId(@PathVariable Long id, @RequestBody Endereco endereco) {

		return ResponseEntity.ok(enderecoService.buscarPorId(id));
	}


}
