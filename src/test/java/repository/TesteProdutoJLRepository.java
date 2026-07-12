package repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.springone.entity.Categoria1;
import com.springone.entity.ProdutoJL;
import com.springone.repository.Categoria1Repository;
import com.springone.repository.ProdutojLRepository;

import contexto.TestContextoSpring;
import jakarta.validation.ConstraintViolationException;

public class TesteProdutoJLRepository extends TestContextoSpring {

	@Autowired
	private ProdutojLRepository produtoRepository;

	@Autowired
	private Categoria1Repository categoriaRepository;

	/*
	 * ========================================================= MÉTODOS AUXILIARES
	 * =========================================================
	 */

	private Categoria1 criarCategoria(String nome) {

		Categoria1 categoria = new Categoria1();
		categoria.setNome(nome);

		return categoriaRepository.saveAndFlush(categoria);
	}

	private ProdutoJL criarProduto(String nome, Double preco, Integer quantidade, Categoria1 categoria) {

		ProdutoJL produto = new ProdutoJL();

		produto.setNome(nome);
		produto.setPreco(preco);
		produto.setQuantidade(quantidade);
		produto.setCategoria1(categoria);

		return produto;
	}

	/*
	 * ========================================================= TESTES DE SAVE
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("Deve salvar produto com sucesso")
	public void deveSalvarProdutoComSucesso() {

		Categoria1 categoria = criarCategoria("Categoria Teste");

		ProdutoJL produto = criarProduto("Notebook", 3500.0, 20, categoria);

		produto = produtoRepository.saveAndFlush(produto);

		assertNotNull(produto);
		assertTrue(produto.getId() > 0);
		assertEquals("Notebook", produto.getNome());
		assertEquals(3500.0, produto.getPreco());
		assertEquals(20, produto.getQuantidade());
		assertNotNull(produto.getCategoria1());
	}

	@Test
	@Order(2)
	@DisplayName("Não deve salvar produto sem nome")
	public void naoDeveSalvarProdutoSemNome() {

		Categoria1 categoria = criarCategoria("Categoria Nome");

		ProdutoJL produto = criarProduto(null, 50.0, 10, categoria);

		assertThrows(ConstraintViolationException.class, () -> {

			produtoRepository.saveAndFlush(produto);

		});
	}

	@Test
	@Order(3)
	@DisplayName("Não deve salvar produto com nome vazio")
	public void naoDeveSalvarProdutoNomeVazio() {

		Categoria1 categoria = criarCategoria("Categoria Nome Vazio");

		ProdutoJL produto = criarProduto("", 50.0, 10, categoria);

		assertThrows(ConstraintViolationException.class, () -> {

			produtoRepository.saveAndFlush(produto);

		});
	}

	@Test
	@Order(4)
	@DisplayName("Não deve salvar produto com preço menor que cinco")
	public void naoDeveSalvarPrecoMenorQueCinco() {

		Categoria1 categoria = criarCategoria("Categoria Preço");

		ProdutoJL produto = criarProduto("Mouse", 4.0, 10, categoria);

		assertThrows(ConstraintViolationException.class, () -> {

			produtoRepository.saveAndFlush(produto);

		});
	}

	@Test
	@Order(5)
	@DisplayName("Não deve salvar produto com quantidade menor que cinco")
	public void naoDeveSalvarQuantidadeMenorQueCinco() {

		Categoria1 categoria = criarCategoria("Categoria Quantidade");

		ProdutoJL produto = criarProduto("Teclado", 100.0, 4, categoria);

		assertThrows(ConstraintViolationException.class, () -> {

			produtoRepository.saveAndFlush(produto);

		});
	}

	@Test
	@Order(6)
	@DisplayName("Não deve salvar produto sem categoria")
	public void naoDeveSalvarSemCategoria() {

		ProdutoJL produto = criarProduto("Produto", 100.0, 10, null);

		assertThrows(Exception.class, () -> {

			produtoRepository.saveAndFlush(produto);

		});
	}

	@Test
	@Order(7)
	@DisplayName("Não deve permitir nome duplicado")
	public void naoDevePermitirNomeDuplicado() {

		Categoria1 categoria = criarCategoria("Categoria Duplicada");

		ProdutoJL produto1 = criarProduto("Notebook Gamer", 5000.0, 10, categoria);

		produtoRepository.saveAndFlush(produto1);

		ProdutoJL produto2 = criarProduto("Notebook Gamer", 3000.0, 15, categoria);

		assertThrows(Exception.class, () -> {

			produtoRepository.saveAndFlush(produto2);

		});
	}

	/*
	 * ========================================================= TESTES DE QUERY
	 * =========================================================
	 */

	@Test
	@Order(8)
	@DisplayName("Deve buscar produto por nome")
	public void deveBuscarProdutoPorNome() {

		Categoria1 categoria = criarCategoria("Categoria Busca");

		ProdutoJL produto = criarProduto("Monitor LG", 800.0, 20, categoria);

		produtoRepository.saveAndFlush(produto);

		ProdutoJL retorno = produtoRepository.findByNome("Monitor");

		assertNotNull(retorno);
		assertEquals("Monitor LG", retorno.getNome());
	}

	@Test
	@Order(9)
	@DisplayName("Deve buscar produto completo")
	public void deveBuscarProdutoCompleto() {

		Categoria1 categoria = criarCategoria("Categoria Join");

		ProdutoJL produto = criarProduto("SSD", 400.0, 15, categoria);

		produto = produtoRepository.saveAndFlush(produto);

		ProdutoJL retorno = produtoRepository.buscarCompleto(produto.getId());

		assertNotNull(retorno);
		assertNotNull(retorno.getCategoria1());
		assertEquals("SSD", retorno.getNome());
	}

	/*
	 * ========================================================= TESTES DE UPDATE
	 * =========================================================
	 */

	@Test
	@Order(10)
	@DisplayName("Deve atualizar produto")
	public void deveAtualizarProduto() {

		Categoria1 categoria = criarCategoria("Categoria Update");

		ProdutoJL produto = criarProduto("Mouse", 150.0, 20, categoria);

		produto = produtoRepository.saveAndFlush(produto);

		produto.setNome("Mouse Gamer");

		produtoRepository.saveAndFlush(produto);

		ProdutoJL atualizado = produtoRepository.findById(produto.getId()).get();

		assertEquals("Mouse Gamer", atualizado.getNome());
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(11)
	@DisplayName("Deve excluir produto")
	public void deveExcluirProduto() {

		Categoria1 categoria = criarCategoria("Categoria Delete");

		ProdutoJL produto = criarProduto("HD", 250.0, 30, categoria);

		produto = produtoRepository.saveAndFlush(produto);

		Long id = produto.getId();

		produtoRepository.deleteById(id);

		assertFalse(produtoRepository.findById(id).isPresent());
	}

	/*
	 * ========================================================= TESTES ENTITY
	 * =========================================================
	 */

	@Test
	@Order(12)
	@DisplayName("Getters e Setters devem funcionar")
	public void gettersESettersDevemFuncionar() {

		Categoria1 categoria = new Categoria1();

		ProdutoJL produto = new ProdutoJL();

		produto.setId(1L);
		produto.setNome("Produto");
		produto.setPreco(50.0);
		produto.setQuantidade(15);
		produto.setCategoria1(categoria);

		assertEquals(1L, produto.getId());
		assertEquals("Produto", produto.getNome());
		assertEquals(50.0, produto.getPreco());
		assertEquals(15, produto.getQuantidade());
		assertEquals(categoria, produto.getCategoria1());
	}

	/*
	 * ========================================================= TESTE FINAL
	 * =========================================================
	 */

	@Test
	@Order(13)
	@DisplayName("SaveAndFlush não deve lançar exceção")
	public void saveAndFlushNaoDeveLancarExcecao() {

		Categoria1 categoria = criarCategoria("Categoria Final");

		ProdutoJL produto = criarProduto("Produto Final", 100.0, 20, categoria);

		assertDoesNotThrow(() -> {

			produtoRepository.saveAndFlush(produto);

		});
	}

}