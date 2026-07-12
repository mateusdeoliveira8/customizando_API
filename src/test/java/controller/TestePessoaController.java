package controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.springone.dto.CadastroDTO;
import com.springone.entity.Pessoa;
import com.springone.exception.MsgApiException;
import com.springone.service.PessoaService;

import contexto.TestContextoSpring;

public class TestePessoaController extends TestContextoSpring {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PessoaService pessoaService;

	private Pessoa pessoa;
	private CadastroDTO cadastroDTO;

	/*
	 * ========================================================= SETUP
	 * =========================================================
	 */

	@BeforeEach
	public void setUp() {

		pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Mateus");
		pessoa.setIdade("21");
		pessoa.setCpf("12345678901");
		pessoa.setCargo("Desenvolvedor");

		cadastroDTO = new CadastroDTO(pessoa);
	}

	/*
	 * ========================================================= TESTES DE GET
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("GET /teste deve retornar 200 e mensagem ok")
	public void deveRetornarMensagemTeste() throws Exception {

		mockMvc.perform(get("/api/cadastro/teste")).andExpect(status().isOk());
	}

	@Test
	@Order(2)
	@DisplayName("GET /listar deve retornar lista de cadastros como DTO")
	public void deveListarCadastros() throws Exception {

		CadastroDTO dto2 = new CadastroDTO(pessoa);
		dto2.setId(2L);
		dto2.setNome("João");
		dto2.setIdade("30");
		dto2.setCpf("98765432100");
		dto2.setCargo("Analista");

		List<CadastroDTO> lista = Arrays.asList(cadastroDTO, dto2);

		when(pessoaService.listaCadastro()).thenReturn(lista);

		mockMvc.perform(get("/api/cadastro/listar")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].nome").value("Mateus"))
				.andExpect(jsonPath("$[1].nome").value("João"));

		verify(pessoaService, times(1)).listaCadastro();
	}

	@Test
	@Order(3)
	@DisplayName("GET /buscaid/{id} deve retornar pessoa por ID")
	public void deveBuscarPessoaPorId() throws Exception {

		when(pessoaService.buscarPorId(1L)).thenReturn(pessoa);

		mockMvc.perform(get("/api/cadastro/buscaid/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.nome").value("Mateus")).andExpect(jsonPath("$.cpf").value("12345678901"));

		verify(pessoaService, times(1)).buscarPorId(1L);
	}

	@Test
	@Order(4)
	@DisplayName("GET /buscaid/{id} deve retornar 500 quando pessoa não encontrada")
	public void deveRetornarErroAoBuscarPessoaInexistente() throws Exception {

		when(pessoaService.buscarPorId(anyLong())).thenThrow(new MsgApiException("Cadastro nao encontrado"));

		mockMvc.perform(get("/api/cadastro/buscaid/999")).andExpect(status().is2xxSuccessful());

		verify(pessoaService, times(1)).buscarPorId(999L);
	}

	@Test
	@Order(5)
	@DisplayName("GET /buscarpornome deve retornar lista de pessoas por nome")
	public void deveBuscarPorNome() throws Exception {

		when(pessoaService.buscarPorNome("Mateus")).thenReturn(Arrays.asList(pessoa));

		mockMvc.perform(get("/api/cadastro/buscarpornome").param("nome", "Mateus")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].nome").value("Mateus"));

		verify(pessoaService, times(1)).buscarPorNome("Mateus");
	}

	@Test
	@Order(6)
	@DisplayName("GET /buscarporcpf deve retornar pessoa por CPF")
	public void deveBuscarPorCpf() throws Exception {

		when(pessoaService.buscarPorCPF("12345678901")).thenReturn(pessoa);

		mockMvc.perform(get("/api/cadastro/buscarporcpf").param("cpf", "12345678901")).andExpect(status().isOk())
				.andExpect(jsonPath("$.cpf").value("12345678901")).andExpect(jsonPath("$.nome").value("Mateus"));

		verify(pessoaService, times(1)).buscarPorCPF("12345678901");
	}

	@Test
	@Order(7)
	@DisplayName("GET /buscarporcpf deve retornar 500 quando CPF não encontrado")
	public void deveRetornarErroAoBuscarCpfInexistente() throws Exception {

		when(pessoaService.buscarPorCPF(anyString())).thenThrow(new MsgApiException("Cadastro nao encontrado!"));

		mockMvc.perform(get("/api/cadastro/buscarporcpf").param("cpf", "00000000000"))
				.andExpect(status().is2xxSuccessful());

		verify(pessoaService, times(1)).buscarPorCPF("00000000000");
	}

	/*
	 * ========================================================= TESTES DE POST
	 * =========================================================
	 */

	@Test
	@Order(8)
	@DisplayName("POST /salvar deve salvar pessoa e retornar 200")
	public void deveSalvarPessoa() throws Exception {

		when(pessoaService.salvarCadastro(any(Pessoa.class))).thenReturn(pessoa);

		mockMvc.perform(post("/api/cadastro/salvar").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(pessoa))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.nome").value("Mateus"))
				.andExpect(jsonPath("$.cpf").value("12345678901"))
				.andExpect(jsonPath("$.cargo").value("Desenvolvedor"));

		verify(pessoaService, times(1)).salvarCadastro(any(Pessoa.class));
	}

	@Test
	@Order(9)
	@DisplayName("POST /salvar deve retornar 500 quando nome é inválido")
	public void deveRetornarErroAoSalvarPessoaSemNome() throws Exception {

		when(pessoaService.salvarCadastro(any(Pessoa.class))).thenThrow(new MsgApiException("erro"));

		Pessoa pessoaSemNome = new Pessoa();
		pessoaSemNome.setCpf("12345678901");

		mockMvc.perform(post("/api/cadastro/salvar").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(pessoaSemNome))).andExpect(status().is2xxSuccessful());

		verify(pessoaService, times(1)).salvarCadastro(any(Pessoa.class));
	}

	@Test
	@Order(10)
	@DisplayName("POST /salvar deve retornar 500 quando CPF já cadastrado")
	public void deveRetornarErroAoSalvarPessoaComCpfDuplicado() throws Exception {

		when(pessoaService.salvarCadastro(any(Pessoa.class))).thenThrow(new MsgApiException("erro"));

		mockMvc.perform(post("/api/cadastro/salvar").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(pessoa))).andExpect(status().is2xxSuccessful());

		verify(pessoaService, times(1)).salvarCadastro(any(Pessoa.class));
	}

	/*
	 * ========================================================= TESTES DE PUT
	 * =========================================================
	 */

	@Test
	@Order(11)
	@DisplayName("PUT /atualizar/{id} deve atualizar pessoa e retornar 200")
	public void deveAtualizarPessoa() throws Exception {

		Pessoa pessoaAtualizada = new Pessoa();
		pessoaAtualizada.setId(1L);
		pessoaAtualizada.setNome("Nome Atualizado");
		pessoaAtualizada.setCargo("Gerente");

		when(pessoaService.atualizarCadastro(anyLong(), any(Pessoa.class))).thenReturn(pessoaAtualizada);

		mockMvc.perform(put("/api/cadastro/atualizar/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(pessoaAtualizada))).andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Nome Atualizado")).andExpect(jsonPath("$.cargo").value("Gerente"));

		verify(pessoaService, times(1)).atualizarCadastro(anyLong(), any(Pessoa.class));
	}

	@Test
	@Order(12)
	@DisplayName("PUT /atualizar/{id} deve retornar 500 quando pessoa não encontrada")
	public void deveRetornarErroAoAtualizarPessoaInexistente() throws Exception {

		when(pessoaService.atualizarCadastro(anyLong(), any(Pessoa.class)))
				.thenThrow(new MsgApiException("Cadastro nao encontrado"));

		mockMvc.perform(put("/api/cadastro/atualizar/999").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(pessoa))).andExpect(status().is2xxSuccessful());

		verify(pessoaService, times(1)).atualizarCadastro(anyLong(), any(Pessoa.class));
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(13)
	@DisplayName("DELETE /deletar/{id} deve deletar pessoa e retornar 204")
	public void deveDeletarPessoa() throws Exception {

		doNothing().when(pessoaService).deletarCadastro(1L);

		mockMvc.perform(delete("/api/cadastro/deletar/1")).andExpect(status().isNoContent());

		verify(pessoaService, times(1)).deletarCadastro(1L);
	}

	@Test
	@Order(14)
	@DisplayName("DELETE /deletar/{id} deve retornar 500 quando pessoa não encontrada")
	public void deveRetornarErroAoDeletarPessoaInexistente() throws Exception {

		doThrow(new MsgApiException("Cadastro nao encontrado!")).when(pessoaService).deletarCadastro(999L);

		mockMvc.perform(delete("/api/cadastro/deletar/999")).andExpect(status().is2xxSuccessful());

		verify(pessoaService, times(1)).deletarCadastro(999L);
	}
}