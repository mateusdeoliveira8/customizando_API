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

import com.springone.entity.ProdutoJL;
import com.springone.service.ProdutoJLService;

import jakarta.validation.Valid;

/**
 * Controller responsável por disponibilizar os endpoints da API para
 * gerenciamento de produtos.
 *
 * <p>
 * Esta classe recebe as requisições HTTP relacionadas aos produtos, encaminha o
 * processamento para a camada de serviço e retorna as respostas ao cliente.
 * </p>
 *
 * @author Mateus Oliveira
 * @since 1.0
 */
@RestController
@RequestMapping("api/produto")
public class ProdutojLController {

	@Autowired
	private ProdutoJLService produtoJLService;

	/**
	 * Endpoint utilizado para testar se a API está funcionando corretamente.
	 *
	 * @return Mensagem indicando que a API está ativa.
	 */
	@GetMapping("/teste")
	public ResponseEntity<String> teste() {
		return ResponseEntity.ok("tudo ok");
	}

	/**
	 * Salva um novo produto.
	 *
	 * @param produtoJL Produto recebido na requisição.
	 * @return Produto salvo com seus dados atualizados.
	 */
	@PostMapping("/salvar")
	public ResponseEntity<ProdutoJL> salvar(@RequestBody @Valid ProdutoJL produtoJL) {
		ProdutoJL produtoJL2 = produtoJLService.salvarProduto(produtoJL);
		return ResponseEntity.ok(produtoJL2);
	}

	/**
	 * Salva uma lista de produtos.
	 *
	 * @param produtos Lista de produtos recebida na requisição.
	 * @return Lista contendo os produtos salvos.
	 */
	@PostMapping("/salvarprodutos")
	public ResponseEntity<List<ProdutoJL>> salvarProdutos(@RequestBody List<ProdutoJL> produtos) {
		List<ProdutoJL> produtosJls = produtoJLService.salvarProdutos(produtos);
		return ResponseEntity.ok(produtosJls);
	}

	/**
	 * Lista todos os produtos cadastrados.
	 *
	 * @return Lista de produtos.
	 */
	@GetMapping("/listar")
	public ResponseEntity<List<ProdutoJL>> listarProduto() {
		return ResponseEntity.ok(produtoJLService.listarProdutos());
	}

	/**
	 * Atualiza os dados de um produto existente.
	 *
	 * @param id        Identificador do produto.
	 * @param produtoJL Novos dados do produto.
	 * @return Produto atualizado.
	 */
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<ProdutoJL> atualizar(@PathVariable Long id, @RequestBody ProdutoJL produtoJL) {

		return ResponseEntity.ok(produtoJLService.atualizarProduto(id, produtoJL));

	}

	/**
	 * Atualiza um produto utilizando o método saveAndFlush().
	 *
	 * @param produtoJL Produto que será atualizado.
	 * @return Produto atualizado.
	 */
	@PutMapping("/atualizar2")
	public ResponseEntity<ProdutoJL> ataualizar2(@RequestBody ProdutoJL produtoJL) {
		return ResponseEntity.ok(produtoJLService.atualizar(produtoJL));
	}

	/**
	 * Remove um produto pelo seu identificador.
	 *
	 * @param id Identificador do produto.
	 * @return Resposta HTTP indicando sucesso na exclusão.
	 */
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		produtoJLService.deletar(id);
		return ResponseEntity.ok().build();
	}

}