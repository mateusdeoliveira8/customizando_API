package controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import com.springone.entity.Endereco;
import com.springone.entity.Pessoa;
import com.springone.exception.MsgApiException;
import com.springone.service.EnderecoService;

import contexto.TestContextoSpring;


public class TesteEnderecoController extends TestContextoSpring {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private EnderecoService enderecoService;

	private Endereco endereco;
	private Pessoa pessoa;

	/*
	 * ========================================================= SETUP
	 * =========================================================
	 */

	@BeforeEach
	public void setUp() {

		pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Pessoa Teste");

		endereco = new Endereco();
		endereco.setId(1L);
		endereco.setRua("Rua das Flores");
		endereco.setNumero(100);
		endereco.setBairro("Centro");
		endereco.setCidade("São Paulo");
		endereco.setUf("SP");
		endereco.setPessoa(pessoa);
	}

	/*
	 * ========================================================= TESTES DE GET
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("GET /teste deve retornar 200 e mensagem ok")
	public void deveRetornarMensagemTeste() throws Exception {

		mockMvc.perform(get("/api/endereco/teste")).andExpect(status().isOk());
	}

	@Test
	@Order(2)
	@DisplayName("GET /listar deve retornar lista de endereços")
	public void deveListarEnderecos() throws Exception {

		Endereco endereco2 = new Endereco();
		endereco2.setId(2L);
		endereco2.setRua("Av. Brasil");
		endereco2.setNumero(200);
		endereco2.setBairro("Jardim");
		endereco2.setCidade("Rio de Janeiro");
		endereco2.setUf("RJ");
		endereco2.setPessoa(pessoa);

		List<Endereco> lista = Arrays.asList(endereco, endereco2);

		when(enderecoService.listarEndereco()).thenReturn(lista);

		mockMvc.perform(get("/api/endereco/listar")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].rua").value("Rua das Flores"))
				.andExpect(jsonPath("$[1].rua").value("Av. Brasil"));

		verify(enderecoService, times(1)).listarEndereco();
	}

	/*
	 * ========================================================= TESTES DE POST
	 * =========================================================
	 */

	@Test
	@Order(3)
	@DisplayName("POST /salvar deve salvar endereço e retornar 200")
	public void deveSalvarEndereco() throws Exception {

		when(enderecoService.salvarEndereco(any(Endereco.class))).thenReturn(endereco);

		mockMvc.perform(post("/api/endereco/salvar").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(endereco))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.rua").value("Rua das Flores"))
				.andExpect(jsonPath("$.numero").value(100)).andExpect(jsonPath("$.bairro").value("Centro"))
				.andExpect(jsonPath("$.cidade").value("São Paulo")).andExpect(jsonPath("$.uf").value("SP"));

		verify(enderecoService, times(1)).salvarEndereco(any(Endereco.class));
	}

	/*
	 * ========================================================= TESTES DE PUT
	 * =========================================================
	 */

	@Test
	@Order(4)
	@DisplayName("PUT /atualizar/{id} deve atualizar endereço e retornar 200")
	public void deveAtualizarEndereco() throws Exception {

		Endereco enderecoAtualizado = new Endereco();
		enderecoAtualizado.setId(1L);
		enderecoAtualizado.setRua("Rua Nova");
		enderecoAtualizado.setNumero(999);
		enderecoAtualizado.setBairro("Bairro Novo");
		enderecoAtualizado.setCidade("São Paulo");
		enderecoAtualizado.setUf("SP");
		enderecoAtualizado.setPessoa(pessoa);

		when(enderecoService.atualizarEndereco(anyLong(), any(Endereco.class))).thenReturn(enderecoAtualizado);

		mockMvc.perform(put("/api/endereco/atualizar/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(enderecoAtualizado))).andExpect(status().isOk())
				.andExpect(jsonPath("$.rua").value("Rua Nova")).andExpect(jsonPath("$.bairro").value("Bairro Novo"));

		verify(enderecoService, times(1)).atualizarEndereco(anyLong(), any(Endereco.class));
	}

	@Test
	@Order(5)
	@DisplayName("PUT /atualizar/{id} deve retornar 500 quando endereço não encontrado")
	public void deveRetornarErroAoAtualizarEnderecoInexistente() throws Exception {

		when(enderecoService.atualizarEndereco(anyLong(), any(Endereco.class)))
				.thenThrow(new MsgApiException("Endereco nao encontrado"));

		mockMvc.perform(put("/api/endereco/atualizar/999").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(endereco))).andExpect(status().is2xxSuccessful());

		verify(enderecoService, times(1)).atualizarEndereco(anyLong(), any(Endereco.class));
	}

	@Test
	@Order(6)
	@DisplayName("PUT /buscarporId/{id} deve retornar endereço encontrado")
	public void deveBuscarEnderecoPorId() throws Exception {

		when(enderecoService.buscarPorId(1L)).thenReturn(endereco);

		mockMvc.perform(put("/api/endereco/buscarporId/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(endereco))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.rua").value("Rua das Flores"));

		verify(enderecoService, times(1)).buscarPorId(1L);
	}

	@Test
	@Order(7)
	@DisplayName("PUT /buscarporId/{id} deve retornar 500 quando endereço não encontrado")
	public void deveRetornarErroAoBuscarEnderecoInexistente() throws Exception {

		when(enderecoService.buscarPorId(anyLong())).thenThrow(new MsgApiException("Endereco nao encontrado"));

		mockMvc.perform(put("/api/endereco/buscarporId/999").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(endereco))).andExpect(status().is2xxSuccessful());

		verify(enderecoService, times(1)).buscarPorId(999L);
	}
}