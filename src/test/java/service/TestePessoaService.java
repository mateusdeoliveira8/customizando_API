package service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import com.springone.entity.Pessoa;
import com.springone.exception.MsgApiException;
import com.springone.repository.PessoaRepository;
import com.springone.service.PessoaService;

import contexto.TestContextoSpring;

@ExtendWith(MockitoExtension.class)
public class TestePessoaService extends TestContextoSpring {

	@Mock
	private PessoaRepository pessoaRepository;

	@InjectMocks
	private PessoaService pessoaService;

	private Pessoa pessoa;

	/*
	 * ========================================================= SETUP
	 * =========================================================
	 */

	@BeforeEach
	public void setUp() {
		pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("João Silva");
		pessoa.setIdade("30");
		pessoa.setCpf("12345678900");
		pessoa.setCargo("Analista");
		pessoa.setEnderecos(new ArrayList<>());
	}

	/*
	 * ========================================================= TESTES DE SAVE
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("Deve salvar cadastro com sucesso")
	public void deveSalvarCadastroComSucesso() {

		when(pessoaRepository.findAll()).thenReturn(Collections.emptyList());
		when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

		Pessoa resultado = pessoaService.salvarCadastro(pessoa);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		assertEquals("João Silva", resultado.getNome());

		verify(pessoaRepository, times(1)).findAll();
		verify(pessoaRepository, times(1)).save(any(Pessoa.class));
	}

	@Test
	@Order(2)
	@DisplayName("Deve lançar exceção ao salvar cadastro com nome nulo")
	public void deveLancarExcecaoAoSalvarCadastroComNomeNulo() {

		pessoa.setNome(null);

		assertThrows(MsgApiException.class, () -> {
			pessoaService.salvarCadastro(pessoa);
		});

		verify(pessoaRepository, times(0)).findAll();
		verify(pessoaRepository, times(0)).save(any(Pessoa.class));
	}

	@Test
	@Order(3)
	@DisplayName("Deve lançar exceção ao salvar cadastro com nome em branco")
	public void deveLancarExcecaoAoSalvarCadastroComNomeEmBranco() {

		pessoa.setNome("   ");

		assertThrows(MsgApiException.class, () -> {
			pessoaService.salvarCadastro(pessoa);
		});

		verify(pessoaRepository, times(0)).save(any(Pessoa.class));
	}

	@Test
	@Order(4)
	@DisplayName("Deve lançar exceção ao salvar cadastro com CPF duplicado")
	public void deveLancarExcecaoAoSalvarCadastroComCpfDuplicado() {

		Pessoa pessoaExistente = new Pessoa();
		pessoaExistente.setId(2L);
		pessoaExistente.setCpf("12345678900");

		when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoaExistente));

		assertThrows(MsgApiException.class, () -> {
			pessoaService.salvarCadastro(pessoa);
		});

		verify(pessoaRepository, times(1)).findAll();
		verify(pessoaRepository, times(0)).save(any(Pessoa.class));
	}

	/*
	 * ========================================================= TESTES DE QUERY
	 * =========================================================
	 */

	@Test
	@Order(5)
	@DisplayName("Deve listar todos os cadastros")
	public void deveListarTodosOsCadastros() {

		Pessoa pessoa2 = new Pessoa();
		pessoa2.setId(2L);
		pessoa2.setNome("Maria Souza");

		when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoa, pessoa2));

		List<Pessoa> resultado = pessoaService.listarCadastro();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		verify(pessoaRepository, times(1)).findAll();
	}

	@Test
	@Order(6)
	@DisplayName("Deve buscar cadastro por ID com sucesso")
	public void deveBuscarCadastroPorIdComSucesso() {

		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

		Pessoa resultado = pessoaService.buscarPorId(1L);

		assertNotNull(resultado);
		assertEquals("João Silva", resultado.getNome());

		verify(pessoaRepository, times(1)).findById(1L);
	}

	@Test
	@Order(7)
	@DisplayName("Deve lançar exceção ao buscar por ID inexistente")
	public void deveLancarExcecaoAoBuscarPorIdInexistente() {

		when(pessoaRepository.findById(anyLong())).thenReturn(Optional.empty());

		// Atenção: o código atual chama pessoa.getId() sobre um objeto nulo,
		// resultando em NullPointerException em vez de MsgApiException.
		assertThrows(NullPointerException.class, () -> {
			pessoaService.buscarPorId(999L);
		});

		verify(pessoaRepository, times(1)).findById(999L);
	}

	@Test
	@Order(8)
	@DisplayName("Deve retornar findAll quando nome for nulo na busca por nome")
	public void deveRetornarFindAllQuandoNomeForNuloNaBuscaPorNome() {

		when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoa));

		List<Pessoa> resultado = pessoaService.buscarPorNome(null);

		assertNotNull(resultado);
		assertEquals(1, resultado.size());

		verify(pessoaRepository, times(1)).findAll();
		verify(pessoaRepository, times(0)).buscarPorNome(anyString());
	}

	@Test
	@Order(9)
	@DisplayName("Deve retornar findAll quando nome for vazio na busca por nome")
	public void deveRetornarFindAllQuandoNomeForVazioNaBuscaPorNome() {

		when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoa));

		List<Pessoa> resultado = pessoaService.buscarPorNome("");

		assertNotNull(resultado);
		assertEquals(1, resultado.size());

		verify(pessoaRepository, times(1)).findAll();
	}

	@Test
	@Order(10)
	@DisplayName("Deve buscar cadastro por nome com sucesso")
	public void deveBuscarCadastroPorNomeComSucesso() {

		when(pessoaRepository.buscarPorNome("João")).thenReturn(Arrays.asList(pessoa));

		List<Pessoa> resultado = pessoaService.buscarPorNome("João");

		assertNotNull(resultado);
		assertEquals(1, resultado.size());

		verify(pessoaRepository, times(1)).buscarPorNome("João");
		verify(pessoaRepository, times(0)).findAll();
	}

	@Test
	@Order(11)
	@DisplayName("Deve buscar cadastro por CPF com sucesso")
	public void deveBuscarCadastroPorCpfComSucesso() {

		when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoa));

		Pessoa resultado = pessoaService.buscarPorCPF("12345678900");

		assertNotNull(resultado);
		assertEquals("João Silva", resultado.getNome());

		verify(pessoaRepository, times(1)).findAll();
	}

	@Test
	@Order(12)
	@DisplayName("Deve lançar exceção ao buscar por CPF inexistente")
	public void deveLancarExcecaoAoBuscarPorCpfInexistente() {

		when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoa));

		assertThrows(MsgApiException.class, () -> {
			pessoaService.buscarPorCPF("00000000000");
		});

		verify(pessoaRepository, times(1)).findAll();
	}

	/*
	 * ========================================================= TESTES DE UPDATE
	 * =========================================================
	 */

	@Test
	@Order(13)
	@DisplayName("Deve atualizar cadastro com sucesso")
	public void deveAtualizarCadastroComSucesso() {

		Pessoa novoCadastro = new Pessoa();
		novoCadastro.setNome("Nome Atualizado");
		novoCadastro.setCargo("Gerente");

		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
		when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(i -> i.getArgument(0));

		Pessoa resultado = pessoaService.atualizarCadastro(1L, novoCadastro);

		assertNotNull(resultado);
		assertEquals("Nome Atualizado", resultado.getNome());
		assertEquals("Gerente", resultado.getCargo());

		verify(pessoaRepository, times(1)).findById(1L);
		verify(pessoaRepository, times(1)).save(any(Pessoa.class));
	}

	@Test
	@Order(14)
	@DisplayName("Deve lançar exceção ao atualizar cadastro inexistente")
	public void deveLancarExcecaoAoAtualizarCadastroInexistente() {

		when(pessoaRepository.findById(anyLong())).thenReturn(Optional.empty());

		Pessoa novoCadastro = new Pessoa();
		novoCadastro.setNome("Qualquer Nome");

		assertThrows(MsgApiException.class, () -> {
			pessoaService.atualizarCadastro(999L, novoCadastro);
		});

		verify(pessoaRepository, times(1)).findById(999L);
		verify(pessoaRepository, times(0)).save(any(Pessoa.class));
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(15)
	@DisplayName("Deve deletar cadastro com sucesso")
	public void deveDeletarCadastroComSucesso() {

		when(pessoaRepository.existsById(1L)).thenReturn(true);
		doNothing().when(pessoaRepository).deleteById(1L);

		assertDoesNotThrow(() -> {
			pessoaService.deletarCadastro(1L);
		});

		verify(pessoaRepository, times(1)).existsById(1L);
		verify(pessoaRepository, times(1)).deleteById(1L);
	}

	@Test
	@Order(16)
	@DisplayName("Deve lançar exceção ao deletar cadastro inexistente")
	public void deveLancarExcecaoAoDeletarCadastroInexistente() {

		when(pessoaRepository.existsById(anyLong())).thenReturn(false);

		assertThrows(MsgApiException.class, () -> {
			pessoaService.deletarCadastro(999L);
		});

		verify(pessoaRepository, times(1)).existsById(999L);
		verify(pessoaRepository, times(0)).deleteById(anyLong());
	}

	/*
	 * ========================================================= TESTES DE DTO
	 * =========================================================
	 */

	@Test
	@Order(17)
	@DisplayName("Deve montar lista de DTOs de cadastro")
	public void deveMontarListaDeDtosDeCadastro() {

		// pessoa já é criada com lista de endereços vazia no setUp().
		// ATENÇÃO: este teste assume que CadastroDTO possui construtor
		// CadastroDTO(Pessoa)
		// e método getEnderecoDTOs() retornando uma List<EnderecoDTO>.
		// Caso a estrutura real seja diferente, envie as classes Endereco, CadastroDTO
		// e EnderecoDTO para ajuste deste teste.

		when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoa));

		var resultado = pessoaService.listaCadastro();

		assertNotNull(resultado);
		assertEquals(1, resultado.size());

		verify(pessoaRepository, times(1)).findAll();
	}
}