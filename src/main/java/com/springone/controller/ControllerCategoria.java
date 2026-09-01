package com.springone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.springone.entity.Categoria1;
import com.springone.service.Categoria1Service;

import jakarta.validation.Valid;

// @RestController = combina @Controller + @ResponseBody: os métodos retornam
// diretamente dados (JSON), não nomes de views/páginas HTML.
// @RequestMapping define o prefixo de URL comum a todos os endpoints da classe.
@RestController
@RequestMapping("/api/categoria")
public class ControllerCategoria {

	@Autowired
	private Categoria1Service categoria1Service;

	// GET /api/categoria/teste -> endpoint simples de "health check" manual,
	// só pra confirmar que a API está no ar.
	@GetMapping("/teste")
	public ResponseEntity<String> teste() {
		return ResponseEntity.ok("tudo ok");
	}

	// POST /api/categoria/salvar -> cria uma nova categoria.
	// @RequestBody: converte o JSON do corpo da requisição em um objeto Categoria1.
	// @Valid: ativa as validações da entidade (@NotBlank, @NotNull, etc. que vimos
	// na Categoria1) ANTES de o método ser executado — se inválido, o Spring já
	// responde 400 automaticamente, sem nem entrar no código do método.
	@PostMapping("/salvar")
	public ResponseEntity<Categoria1> salvar(@RequestBody @Valid Categoria1 categoria1) {
		Categoria1 categoria2 = categoria1Service.salvarcategoria(categoria1);
		return ResponseEntity.ok(categoria2);
	}

	// POST /api/categoria/salvarCategorias -> salva uma LISTA de categorias de uma
	// vez.
	// Repare que aqui NÃO tem @Valid — as validações da entidade não são
	// aplicadas nesse endpoint (inconsistência em relação ao /salvar acima).
	@PostMapping("/salvarCategorias")
	public ResponseEntity<List<Categoria1>> salvarCategorias(@RequestBody List<Categoria1> categoria1s) {
		List<Categoria1> categoriasSalvas = categoria1Service.salvarCategorias(categoria1s);
		return ResponseEntity.ok(categoriasSalvas);
	}

	// GET /api/categoria/listar -> retorna todas as categorias cadastradas.
	@GetMapping("/listar")
	public ResponseEntity<List<Categoria1>> listarCategorias() {
		return ResponseEntity.ok(categoria1Service.listarCategorias());
	}

	// PUT /api/categoria/atualizar/{id} -> atoualiza uma categoria específica pelo
	// ID.
	// @PathVariable extrai o {id} da URL.
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<Categoria1> atualizarCategoria(@PathVariable Long id, @RequestBody Categoria1 categoria1) {
		return ResponseEntity.ok(categoria1Service.atualizarCategoria(id, categoria1));
	}

	// PUT /api/categoria/atualizar2 -> OUTRO endpoint de atualização, mas sem
	// receber o ID pela URL (provavelmente o ID vem dentro do próprio corpo JSON,
	// já que Categoria1 tem o campo "id"). Chama um método DIFERENTE do service
	// ("atualizar" em vez de "atualizarCategoria") — parece um endpoint duplicado/
	// alternativo ao de cima, talvez sobra de refatoração ou teste.
	@PutMapping("/atualizar2")
	public ResponseEntity<Categoria1> atualizar(@RequestBody Categoria1 categoria1) {
		return ResponseEntity.ok(categoria1Service.atualizar(categoria1));
	}

	// DELETE /api/categoria/deletar/{id} -> remove uma categoria pelo ID.
	// Retorna 200 OK com corpo vazio (Void).
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		categoria1Service.deletar(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "/listaPaginada", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<Categoria1>> listaPaginada(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<Categoria1> pagina = categoria1Service.listaPaginada(page, size);
		return ResponseEntity.ok(pagina.getContent());
	}
}