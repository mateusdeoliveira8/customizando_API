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
import com.springone.entity.ProdutoJL;
import com.springone.exception.MsgApiException;
import com.springone.repository.ProdutojLRepository;
import com.springone.service.ProdutoJLService;

import contexto.TestContextoSpring;

@ExtendWith(MockitoExtension.class)
public class TesteProdutoJLService extends TestContextoSpring {

	@Mock
	private ProdutojLRepository produtojLRepository;

	@InjectMocks
	private ProdutoJLService produtoJLService;

	private ProdutoJL produto;
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

		produto = new ProdutoJL();
		produto.setId(1L);
		produto.setNome("Notebook");
		produto.setPreco(2500.0);
		produto.setQuantidade(10);
		produto.setCategoria1(categoria);
	}

	/*
	 * ========================================================= TESTES DE SAVE
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("Deve salvar produto com sucesso")
	public void deveSalvarProdutoComSucesso() {

		when(produtojLRepository.buscarCompleto(1L)).thenReturn(produto);

		ProdutoJL resultado = produtoJLService.salvarProduto(produto);

		assertNotNull(resultado);
		assertEquals("Notebook", resultado.getNome());
		assertEquals("Eletrônicos", resultado.getCategoria1().getNome());

		verify(produtojLRepository, times(1)).save(any(ProdutoJL.class));
		verify(produtojLRepository, times(1)).buscarCompleto(1L);
	}

	@Test
	@Order(2)
	@DisplayName("Deve salvar lista de produtos com sucesso")
	public void deveSalvarListaDeProdutosComSucesso() {

		ProdutoJL produto2 = new ProdutoJL();
		produto2.setId(2L);
		produto2.setNome("Mouse");

		List<ProdutoJL> lista = Arrays.asList(produto, produto2);

		when(produtojLRepository.saveAll(any())).thenReturn(lista);

		List<ProdutoJL> resultado = produtoJLService.salvarProdutos(lista);

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		verify(produtojLRepository, times(1)).saveAll(any());
	}

	/*
	 * ========================================================= TESTES DE QUERY
	 * =========================================================
	 */

	@Test
	@Order(3)
	@DisplayName("Deve listar todos os produtos")
	public void deveListarTodosOsProdutos() {

		ProdutoJL produto2 = new ProdutoJL();
		produto2.setId(2L);
		produto2.setNome("Teclado");

		when(produtojLRepository.findAll()).thenReturn(Arrays.asList(produto, produto2));

		List<ProdutoJL> resultado = produtoJLService.listarProdutos();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		verify(produtojLRepository, times(1)).findAll();
	}

	/*
	 * ========================================================= TESTES DE UPDATE
	 * =========================================================
	 */

	@Test
	@Order(4)
	@DisplayName("Deve atualizar produto com sucesso")
	public void deveAtualizarProdutoComSucesso() {

		ProdutoJL produtoAtualizado = new ProdutoJL();
		produtoAtualizado.setNome("Notebook Atualizado");
		produtoAtualizado.setPreco(3000.0);
		produtoAtualizado.setQuantidade(5);

		when(produtojLRepository.findById(1L)).thenReturn(Optional.of(produto));
		when(produtojLRepository.save(any(ProdutoJL.class))).thenAnswer(i -> i.getArgument(0));

		ProdutoJL resultado = produtoJLService.atualizarProduto(1L, produtoAtualizado);

		assertNotNull(resultado);
		assertEquals("Notebook Atualizado", resultado.getNome());
		assertEquals(3000.0, resultado.getPreco());
		assertEquals(5, resultado.getQuantidade());

		verify(produtojLRepository, times(1)).findById(1L);
		verify(produtojLRepository, times(1)).save(any(ProdutoJL.class));
	}

	@Test
	@Order(5)
	@DisplayName("Deve lançar exceção ao atualizar produto inexistente")
	public void deveLancarExcecaoAoAtualizarProdutoInexistente() {

		when(produtojLRepository.findById(anyLong())).thenReturn(Optional.empty());

		ProdutoJL produtoAtualizado = new ProdutoJL();
		produtoAtualizado.setNome("Qualquer Nome");

		assertThrows(MsgApiException.class, () -> {
			produtoJLService.atualizarProduto(999L, produtoAtualizado);
		});

		verify(produtojLRepository, times(1)).findById(999L);
		verify(produtojLRepository, times(0)).save(any(ProdutoJL.class));
	}

	@Test
	@Order(6)
	@DisplayName("Deve atualizar produto via saveAndFlush")
	public void deveAtualizarProdutoViaSaveAndFlush() {

		when(produtojLRepository.saveAndFlush(any(ProdutoJL.class))).thenReturn(produto);

		ProdutoJL resultado = produtoJLService.atualizar(produto);

		assertNotNull(resultado);
		assertEquals("Notebook", resultado.getNome());

		verify(produtojLRepository, times(1)).saveAndFlush(any(ProdutoJL.class));
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(7)
	@DisplayName("Deve deletar produto com sucesso")
	public void deveDeletarProdutoComSucesso() {

		when(produtojLRepository.findById(1L)).thenReturn(Optional.of(produto));
		doNothing().when(produtojLRepository).deleteById(1L);

		assertDoesNotThrow(() -> {
			produtoJLService.deletar(1L);
		});

		verify(produtojLRepository, times(1)).findById(1L);
		verify(produtojLRepository, times(1)).deleteById(1L);
	}

	@Test
	@Order(8)
	@DisplayName("Deve lançar exceção ao deletar produto inexistente")
	public void deveLancarExcecaoAoDeletarProdutoInexistente() {

		when(produtojLRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(MsgApiException.class, () -> {
			produtoJLService.deletar(999L);
		});

		verify(produtojLRepository, times(1)).findById(999L);
		verify(produtojLRepository, times(0)).deleteById(anyLong());
	}
}