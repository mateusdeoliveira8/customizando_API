package controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springone.entity.Categoria1;
import com.springone.entity.ProdutoJL;
import com.springone.exception.MsgApiException;
import com.springone.service.ProdutoJLService;

import contexto.TestContextoSpring;

public class TesteProdutoJLController extends TestContextoSpring {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
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
		produto.setPreco(3500.00);
		produto.setQuantidade(10);
		produto.setCategoria1(categoria);
	}

	/*
	 * ========================================================= TESTES DE GET
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("GET /teste deve retornar 200 e mensagem ok")
	public void deveRetornarMensagemTeste() throws Exception {

		mockMvc.perform(get("/api/produto/teste")).andExpect(status().isOk());
	}

	@Test
	@Order(2)
	@DisplayName("GET /listar deve retornar lista de produtos")
	public void deveListarProdutos() throws Exception {

		ProdutoJL produto2 = new ProdutoJL();
		produto2.setId(2L);
		produto2.setNome("Mouse");
		produto2.setPreco(150.00);
		produto2.setQuantidade(50);
		produto2.setCategoria1(categoria);

		List<ProdutoJL> lista = Arrays.asList(produto, produto2);

		when(produtoJLService.listarProdutos()).thenReturn(lista);

		mockMvc.perform(get("/api/produto/listar")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].nome").value("Notebook"))
				.andExpect(jsonPath("$[1].nome").value("Mouse"));

		verify(produtoJLService, times(1)).listarProdutos();
	}

	/*
	 * ========================================================= TESTES DE POST
	 * =========================================================
	 */

	@Test
	@Order(3)
	@DisplayName("POST /salvar deve salvar produto e retornar 200")
	public void deveSalvarProduto() throws Exception {

		when(produtoJLService.salvarProduto(any(ProdutoJL.class))).thenReturn(produto);

		mockMvc.perform(post("/api/produto/salvar").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(produto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Notebook")).andExpect(jsonPath("$.preco").value(3500.00))
				.andExpect(jsonPath("$.quantidade").value(10));

		verify(produtoJLService, times(1)).salvarProduto(any(ProdutoJL.class));
	}

	@Test
	@Order(4)
	@DisplayName("POST /salvarprodutos deve salvar lista de produtos e retornar 200")
	public void deveSalvarListaDeProdutos() throws Exception {

		ProdutoJL produto2 = new ProdutoJL();
		produto2.setId(2L);
		produto2.setNome("Teclado");
		produto2.setPreco(200.00);
		produto2.setQuantidade(20);
		produto2.setCategoria1(categoria);

		List<ProdutoJL> lista = Arrays.asList(produto, produto2);

		when(produtoJLService.salvarProdutos(any())).thenReturn(lista);

		mockMvc.perform(post("/api/produto/salvarprodutos").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lista))).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].nome").value("Notebook"))
				.andExpect(jsonPath("$[1].nome").value("Teclado"));

		verify(produtoJLService, times(1)).salvarProdutos(any());
	}

	/*
	 * ========================================================= TESTES DE PUT
	 * =========================================================
	 */

	@Test
	@Order(5)
	@DisplayName("PUT /atualizar/{id} deve atualizar produto e retornar 200")
	public void deveAtualizarProduto() throws Exception {

		ProdutoJL produtoAtualizado = new ProdutoJL();
		produtoAtualizado.setId(1L);
		produtoAtualizado.setNome("Notebook Gamer");
		produtoAtualizado.setPreco(7000.00);
		produtoAtualizado.setQuantidade(5);
		produtoAtualizado.setCategoria1(categoria);

		when(produtoJLService.atualizarProduto(anyLong(), any(ProdutoJL.class))).thenReturn(produtoAtualizado);

		mockMvc.perform(put("/api/produto/atualizar/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(produtoAtualizado))).andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Notebook Gamer")).andExpect(jsonPath("$.preco").value(7000.00))
				.andExpect(jsonPath("$.quantidade").value(5));

		verify(produtoJLService, times(1)).atualizarProduto(anyLong(), any(ProdutoJL.class));
	}

	@Test
	@Order(6)
	@DisplayName("PUT /atualizar/{id} deve retornar 500 quando produto não encontrado")
	public void deveRetornarErroAoAtualizarProdutoInexistente() throws Exception {

		when(produtoJLService.atualizarProduto(anyLong(), any(ProdutoJL.class)))
				.thenThrow(new MsgApiException("Produto nao encontrado"));

		mockMvc.perform(put("/api/produto/atualizar/999").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(produto))).andExpect(status().is2xxSuccessful());

		verify(produtoJLService, times(1)).atualizarProduto(anyLong(), any(ProdutoJL.class));
	}

	@Test
	@Order(7)
	@DisplayName("PUT /atualizar2 deve atualizar produto via saveAndFlush e retornar 200")
	public void deveAtualizarProduto2() throws Exception {

		when(produtoJLService.atualizar(any(ProdutoJL.class))).thenReturn(produto);

		mockMvc.perform(put("/api/produto/atualizar2").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(produto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Notebook")).andExpect(jsonPath("$.preco").value(3500.00));

		verify(produtoJLService, times(1)).atualizar(any(ProdutoJL.class));
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(8)
	@DisplayName("DELETE /deletar/{id} deve deletar produto e retornar 200")
	public void deveDeletarProduto() throws Exception {

		doNothing().when(produtoJLService).deletar(1L);

		mockMvc.perform(delete("/api/produto/deletar/1")).andExpect(status().isOk());

		verify(produtoJLService, times(1)).deletar(1L);
	}

	@Test
	@Order(9)
	@DisplayName("DELETE /deletar/{id} deve retornar 500 quando produto não encontrado")
	public void deveRetornarErroAoDeletarProdutoInexistente() throws Exception {

		doThrow(new MsgApiException("Nao existe produto com esse ID")).when(produtoJLService).deletar(999L);

		mockMvc.perform(delete("/api/produto/deletar/999")).andExpect(status().is2xxSuccessful());

		verify(produtoJLService, times(1)).deletar(999L);
	}
}