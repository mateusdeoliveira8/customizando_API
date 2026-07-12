package repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.springone.entity.Categoria1;
import com.springone.repository.Categoria1Repository;

import contexto.TestContextoSpring;

public class TesteCategoria1Repository extends TestContextoSpring {

	@Autowired
	private Categoria1Repository categoria1Repository;

	/*
	 * ========================================================= MÉTODO AUXILIAR
	 * =========================================================
	 */

	private Categoria1 criarCategoria(String nome) {

		Categoria1 categoria = new Categoria1();
		categoria.setNome(nome);

		return categoria;
	}

	/*
	 * ========================================================= TESTES DE SAVE
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("Deve salvar categoria com sucesso")
	public void deveSalvarCategoriaComSucesso() {

		Categoria1 categoria = criarCategoria("Eletrônicos");

		categoria = categoria1Repository.saveAndFlush(categoria);

		assertNotNull(categoria);
		assertNotNull(categoria.getId());

		assertEquals("Eletrônicos", categoria.getNome());
	}

	/*
	 * ========================================================= TESTES DE QUERY
	 * =========================================================
	 */

	@Test
	@Order(2)
	@DisplayName("Deve buscar todos as categorias")
	public void deveBuscarTodasAsCategorias() {

		Categoria1 categoria = criarCategoria("Informática");

		categoria1Repository.saveAndFlush(categoria);

		List<Categoria1> lista = categoria1Repository.findAll();

		assertNotNull(lista);
		assertFalse(lista.isEmpty());
	}

	@Test
	@Order(3)
	@DisplayName("Deve buscar categoria por ID")
	public void deveBuscarCategoriaPorId() {

		Categoria1 categoria = criarCategoria("Roupas");

		categoria = categoria1Repository.saveAndFlush(categoria);

		Optional<Categoria1> encontrada = categoria1Repository.findById(categoria.getId());

		assertTrue(encontrada.isPresent());
		assertEquals("Roupas", encontrada.get().getNome());
	}

	@Test
	@Order(4)
	@DisplayName("Deve retornar vazio para ID inexistente")
	public void deveRetornarVazioParaIdInexistente() {

		Optional<Categoria1> encontrada = categoria1Repository.findById(999999L);

		assertFalse(encontrada.isPresent());
	}

	/*
	 * ========================================================= TESTES DE UPDATE
	 * =========================================================
	 */

	@Test
	@Order(5)
	@DisplayName("Deve atualizar categoria")
	public void deveAtualizarCategoria() {

		Categoria1 categoria = criarCategoria("Nome Antigo Cat");

		categoria = categoria1Repository.saveAndFlush(categoria);

		categoria.setNome("Nome Novo Cat");

		categoria1Repository.saveAndFlush(categoria);

		Categoria1 categoriaAtualizada = categoria1Repository.findById(categoria.getId()).orElseThrow();

		assertEquals("Nome Novo Cat", categoriaAtualizada.getNome());
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(6)
	@DisplayName("Deve excluir categoria")
	public void deveExcluirCategoria() {

		Categoria1 categoria = criarCategoria("Categoria Delete");

		categoria = categoria1Repository.saveAndFlush(categoria);

		Long id = categoria.getId();

		categoria1Repository.deleteById(id);

		assertFalse(categoria1Repository.findById(id).isPresent());
	}

	/*
	 * ========================================================= TESTES ENTITY
	 * =========================================================
	 */

	@Test
	@Order(7)
	@DisplayName("Getters e Setters devem funcionar")
	public void gettersESettersDevemFuncionar() {

		Categoria1 categoria = new Categoria1();

		categoria.setId(1L);
		categoria.setNome("Calçados");

		assertEquals(1L, categoria.getId());
		assertEquals("Calçados", categoria.getNome());
		assertNotNull(categoria.getProdutoJLs());
		assertTrue(categoria.getProdutoJLs().isEmpty());
	}

	/*
	 * ========================================================= TESTE FINAL
	 * =========================================================
	 */

	@Test
	@Order(8)
	@DisplayName("SaveAndFlush não deve lançar exceção")
	public void saveAndFlushNaoDeveLancarExcecao() {

		Categoria1 categoria = criarCategoria("Categoria Final");

		assertDoesNotThrow(() -> {
			categoria1Repository.saveAndFlush(categoria);
		});
	}
}