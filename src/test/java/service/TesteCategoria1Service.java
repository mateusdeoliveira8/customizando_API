package service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springone.entity.Categoria1;
import com.springone.exception.MsgApiException;
import com.springone.repository.Categoria1Repository;
import com.springone.service.Categoria1Service;

import contexto.TestContextoSpring;

@ExtendWith(MockitoExtension.class)
public class TesteCategoria1Service extends TestContextoSpring {

	@Mock
	private Categoria1Repository categoria1Repository;

	@InjectMocks
	private Categoria1Service categoria1Service;

	private Categoria1 categoria;

	/*
	 * ========================================================= SETUP
	 * =========================================================
	 */

	@BeforeEach
	public void setUp() {
		categoria = new Categoria1();
		categoria.setId(1L);
		categoria.setNome("Eletrônicos");
	}

	/*
	 * ========================================================= TESTES DE SAVE
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("Deve salvar categoria com sucesso")
	public void deveSalvarCategoriaComSucesso() {

		when(categoria1Repository.save(any(Categoria1.class))).thenReturn(categoria);

		Categoria1 resultado = categoria1Service.salvarcategoria(categoria);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		assertEquals("Eletrônicos", resultado.getNome());

		verify(categoria1Repository, times(1)).save(any(Categoria1.class));
	}

	@Test
	@Order(2)
	@DisplayName("Deve salvar lista de categorias com sucesso")
	public void deveSalvarListaDeCategoriasComSucesso() {

		Categoria1 categoria2 = new Categoria1();
		categoria2.setId(2L);
		categoria2.setNome("Informática");

		List<Categoria1> lista = Arrays.asList(categoria, categoria2);

		when(categoria1Repository.saveAll(any())).thenReturn(lista);

		List<Categoria1> resultado = categoria1Service.salvarCategorias(lista);

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		verify(categoria1Repository, times(1)).saveAll(any());
	}

	/*
	 * ========================================================= TESTES DE QUERY
	 * =========================================================
	 */

	@Test
	@Order(3)
	@DisplayName("Deve listar todas as categorias")
	public void deveListarTodasAsCategorias() {

		Categoria1 categoria2 = new Categoria1();
		categoria2.setId(2L);
		categoria2.setNome("Roupas");

		when(categoria1Repository.findAll()).thenReturn(Arrays.asList(categoria, categoria2));

		List<Categoria1> resultado = categoria1Service.listarCategorias();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		verify(categoria1Repository, times(1)).findAll();
	}

	/*
	 * ========================================================= TESTES DE UPDATE
	 * =========================================================
	 */

	@Test
	@Order(4)
	@DisplayName("Deve atualizar categoria com sucesso")
	public void deveAtualizarCategoriaComSucesso() {

		Categoria1 categoriaAtualizada = new Categoria1();
		categoriaAtualizada.setNome("Nome Atualizado");

		when(categoria1Repository.findById(1L)).thenReturn(Optional.of(categoria));
		when(categoria1Repository.save(any(Categoria1.class))).thenAnswer(i -> i.getArgument(0));

		Categoria1 resultado = categoria1Service.atualizarCategoria(1L, categoriaAtualizada);

		assertNotNull(resultado);
		assertEquals("Nome Atualizado", resultado.getNome());

		verify(categoria1Repository, times(1)).findById(1L);
		verify(categoria1Repository, times(1)).save(any(Categoria1.class));
	}

	@Test
	@Order(5)
	@DisplayName("Deve lançar exceção ao atualizar categoria inexistente")
	public void deveLancarExcecaoAoAtualizarCategoriaInexistente() {

		when(categoria1Repository.findById(anyLong())).thenReturn(Optional.empty());

		Categoria1 categoriaAtualizada = new Categoria1();
		categoriaAtualizada.setNome("Qualquer Nome");

		assertThrows(MsgApiException.class, () -> {
			categoria1Service.atualizarCategoria(999L, categoriaAtualizada);
		});

		verify(categoria1Repository, times(1)).findById(999L);
		verify(categoria1Repository, times(0)).save(any(Categoria1.class));
	}

	@Test
	@Order(6)
	@DisplayName("Deve atualizar categoria via saveAndFlush")
	public void deveAtualizarCategoriaViaSaveAndFlush() {

		when(categoria1Repository.saveAndFlush(any(Categoria1.class))).thenReturn(categoria);

		Categoria1 resultado = categoria1Service.atualizar(categoria);

		assertNotNull(resultado);
		assertEquals("Eletrônicos", resultado.getNome());

		verify(categoria1Repository, times(1)).saveAndFlush(any(Categoria1.class));
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(7)
	@DisplayName("Deve deletar categoria com sucesso")
	public void deveDeletarCategoriaComSucesso() {

		when(categoria1Repository.findById(1L)).thenReturn(Optional.of(categoria));
		doNothing().when(categoria1Repository).deleteById(1L);

		assertDoesNotThrow(() -> {
			categoria1Service.deletar(1L);
		});

		verify(categoria1Repository, times(1)).findById(1L);
		verify(categoria1Repository, times(1)).deleteById(1L);
	}

	@Test
	@Order(8)
	@DisplayName("Deve lançar exceção ao deletar categoria inexistente")
	public void deveLancarExcecaoAoDeletarCategoriaInexistente() {

		when(categoria1Repository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(MsgApiException.class, () -> {
			categoria1Service.deletar(999L);
		});

		verify(categoria1Repository, times(1)).findById(999L);
		verify(categoria1Repository, times(0)).deleteById(anyLong());
	}
}