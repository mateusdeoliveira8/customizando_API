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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springone.dto.CadastroDTO;
import com.springone.entity.Pessoa;
import com.springone.service.PessoaService;

@RestController
@RequestMapping("/api/cadastro")
public class PessoaCadastro {

	@Autowired
	private PessoaService pessoaService;

	// ==========================
	// TESTE DA API
	// ==========================
	@GetMapping("/teste")
	public ResponseEntity<String> teste() {
		return ResponseEntity.ok("Esta funcionado");
	}

	// ==========================
	// SALVAR NOVO CADASTRO
	// ==========================
	@PostMapping("/salvar")
	public ResponseEntity<Pessoa> salvarCadastro(@RequestBody Pessoa pessoa) {
		Pessoa cadastrosalvo = pessoaService.salvarCadastro(pessoa);
		return ResponseEntity.ok(cadastrosalvo);
	}

	// ==========================
	// LISTAR TODOS OS CADASTROS
	// ==========================
	@GetMapping("/listar")
	public List<CadastroDTO> listarCadastros() {

		return pessoaService.listaCadastro();


	}

	// ==========================
	// DELETAR CADASTRO POR ID
	// ==========================
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deleteID(@PathVariable(name = "id") Long id) {

		pessoaService.deletarCadastro(id);

		return ResponseEntity.noContent().build();
	}

	// ==========================
	// ATUALIZAR CADASTRO
	// ==========================
	@PutMapping("/atualizar/{id}")
	public Pessoa atualizar(@PathVariable Long id, @RequestBody Pessoa pessoa) {

		return pessoaService.atualizarCadastro(id, pessoa);
	}

	// ==========================
	// BUSCAR CADASTRO POR ID
	// ==========================
	@GetMapping("/buscaid/{id}")
	public ResponseEntity<Pessoa> buscaPorId(@PathVariable Long id) {

		return ResponseEntity.ok(pessoaService.buscarPorId(id));
	}

	// ==========================
	// BUSCAR CADASTRO POR NOME
	// ==========================
	@GetMapping("/buscarpornome")
	public List<Pessoa> buscarPorNome(@RequestParam String nome) {

		return pessoaService.buscarPorNome(nome);
	}

	@GetMapping("/buscarporcpf")
	public ResponseEntity<Pessoa> buscarPorCpf(@RequestParam String cpf) {
		Pessoa cadastrobuscaCPF = pessoaService.buscarPorCPF(cpf);

		return ResponseEntity.ok(cadastrobuscaCPF);
	}

}
