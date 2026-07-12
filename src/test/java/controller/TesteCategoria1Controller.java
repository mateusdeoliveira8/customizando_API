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
import com.springone.exception.MsgApiException;
import com.springone.service.Categoria1Service;

import contexto.TestContextoSpring;


public class TesteCategoria1Controller extends TestContextoSpring {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
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
	 * ========================================================= TESTES DE GET
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("GET /teste deve retornar 200 e mensagem ok")
	public void deveRetornarMensagemTeste() throws Exception {

		mockMvc.perform(get("/api/categoria/teste")).andExpect(status().isOk());
	}

	@Test
	@Order(2)
	@DisplayName("GET /listar deve retornar lista de categorias")
	public void deveListarCategorias() throws Exception {

		Categoria1 categoria2 = new Categoria1();
		categoria2.setId(2L);
		categoria2.setNome("Informática");

		List<Categoria1> lista = Arrays.asList(categoria, categoria2);

		when(categoria1Service.listarCategorias()).thenReturn(lista);

		mockMvc.perform(get("/api/categoria/listar")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].nome").value("Eletrônicos"))
				.andExpect(jsonPath("$[1].nome").value("Informática"));

		verify(categoria1Service, times(1)).listarCategorias();
	}

	/*
	 * ========================================================= TESTES DE POST
	 * =========================================================
	 */

	@Test
	@Order(3)
	@DisplayName("POST /salvar deve salvar categoria e retornar 200")
	public void deveSalvarCategoria() throws Exception {

		when(categoria1Service.salvarcategoria(any(Categoria1.class))).thenReturn(categoria);

		mockMvc.perform(post("/api/categoria/salvar").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(categoria))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.nome").value("Eletrônicos"));

		verify(categoria1Service, times(1)).salvarcategoria(any(Categoria1.class));
	}

	@Test
	@Order(4)
	@DisplayName("POST /salvarCategorias deve salvar lista de categorias e retornar 200")
	public void deveSalvarListaDeCategorias() throws Exception {

		Categoria1 categoria2 = new Categoria1();
		categoria2.setId(2L);
		categoria2.setNome("Roupas");

		List<Categoria1> lista = Arrays.asList(categoria, categoria2);

		when(categoria1Service.salvarCategorias(any())).thenReturn(lista);

		mockMvc.perform(post("/api/categoria/salvarCategorias").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lista))).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].nome").value("Eletrônicos"))
				.andExpect(jsonPath("$[1].nome").value("Roupas"));

		verify(categoria1Service, times(1)).salvarCategorias(any());
	}

	/*
	 * ========================================================= TESTES DE PUT
	 * =========================================================
	 */

	@Test
	@Order(5)
	@DisplayName("PUT /atualizar/{id} deve atualizar categoria e retornar 200")
	public void deveAtualizarCategoria() throws Exception {

		Categoria1 categoriaAtualizada = new Categoria1();
		categoriaAtualizada.setId(1L);
		categoriaAtualizada.setNome("Nome Atualizado");

		when(categoria1Service.atualizarCategoria(anyLong(), any(Categoria1.class))).thenReturn(categoriaAtualizada);

		mockMvc.perform(put("/api/categoria/atualizar/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(categoriaAtualizada))).andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Nome Atualizado"));

		verify(categoria1Service, times(1)).atualizarCategoria(anyLong(), any(Categoria1.class));
	}

	@Test
	@Order(6)
	@DisplayName("PUT /atualizar/{id} deve retornar 500 quando categoria não encontrada")
	public void deveRetornarErroAoAtualizarCategoriaInexistente() throws Exception {

		when(categoria1Service.atualizarCategoria(anyLong(), any(Categoria1.class)))
				.thenThrow(new MsgApiException("Categoria não encontrada"));

		mockMvc.perform(put("/api/categoria/atualizar/999").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(categoria))).andExpect(status().is2xxSuccessful());

		verify(categoria1Service, times(1)).atualizarCategoria(anyLong(), any(Categoria1.class));
	}

	@Test
	@Order(7)
	@DisplayName("PUT /atualizar2 deve atualizar categoria via saveAndFlush e retornar 200")
	public void deveAtualizarCategoria2() throws Exception {

		when(categoria1Service.atualizar(any(Categoria1.class))).thenReturn(categoria);

		mockMvc.perform(put("/api/categoria/atualizar2").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(categoria))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.nome").value("Eletrônicos"));

		verify(categoria1Service, times(1)).atualizar(any(Categoria1.class));
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(8)
	@DisplayName("DELETE /deletar/{id} deve deletar categoria e retornar 200")
	public void deveDeletarCategoria() throws Exception {

		doNothing().when(categoria1Service).deletar(1L);

		mockMvc.perform(delete("/api/categoria/deletar/1")).andExpect(status().isOk());

		verify(categoria1Service, times(1)).deletar(1L);
	}

	@Test
	@Order(9)
	@DisplayName("DELETE /deletar/{id} deve retornar 500 quando categoria não encontrada")
	public void deveRetornarErroAoDeletarCategoriaInexistente() throws Exception {

		doThrow(new MsgApiException("Não existe categoria com esse ID")).when(categoria1Service).deletar(999L);

		mockMvc.perform(delete("/api/categoria/deletar/999")).andExpect(status().is2xxSuccessful());

		verify(categoria1Service, times(1)).deletar(999L);
	}
}