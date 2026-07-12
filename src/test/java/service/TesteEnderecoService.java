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

import com.springone.entity.Endereco;
import com.springone.entity.Pessoa;
import com.springone.exception.MsgApiException;
import com.springone.repository.EnderecoRepository;
import com.springone.service.EnderecoService;

import contexto.TestContextoSpring;

@ExtendWith(MockitoExtension.class)
public class TesteEnderecoService extends TestContextoSpring {

	@Mock
	private EnderecoRepository enderecoRepository;

	@InjectMocks
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
	 * ========================================================= TESTES DE SAVE
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("Deve salvar endereço com sucesso")
	public void deveSalvarEnderecoComSucesso() {

		when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

		Endereco resultado = enderecoService.salvarEndereco(endereco);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		assertEquals("Rua das Flores", resultado.getRua());
		assertEquals(100, resultado.getNumero());
		assertEquals("Centro", resultado.getBairro());
		assertEquals("São Paulo", resultado.getCidade());
		assertEquals("SP", resultado.getUf());
		assertNotNull(resultado.getPessoa());

		verify(enderecoRepository, times(1)).save(any(Endereco.class));
	}

	/*
	 * ========================================================= TESTES DE QUERY
	 * =========================================================
	 */

	@Test
	@Order(2)
	@DisplayName("Deve listar todos os endereços")
	public void deveListarTodosOsEnderecos() {

		Endereco endereco2 = new Endereco();
		endereco2.setId(2L);
		endereco2.setRua("Av. Brasil");
		endereco2.setPessoa(pessoa);

		when(enderecoRepository.findAll()).thenReturn(Arrays.asList(endereco, endereco2));

		List<Endereco> resultado = enderecoService.listarEndereco();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		verify(enderecoRepository, times(1)).findAll();
	}

	@Test
	@Order(3)
	@DisplayName("Deve buscar endereço por ID com sucesso")
	public void deveBuscarEnderecoPorIdComSucesso() {

		when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

		Endereco resultado = enderecoService.buscarPorId(1L);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		assertEquals("Rua das Flores", resultado.getRua());

		verify(enderecoRepository, times(1)).findById(1L);
	}

	@Test
	@Order(4)
	@DisplayName("Deve lançar exceção ao buscar endereço inexistente")
	public void deveLancarExcecaoAoBuscarEnderecoInexistente() {

		when(enderecoRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(MsgApiException.class, () -> {
			enderecoService.buscarPorId(999L);
		});

		verify(enderecoRepository, times(1)).findById(999L);
	}

	/*
	 * ========================================================= TESTES DE UPDATE
	 * =========================================================
	 */

	@Test
	@Order(5)
	@DisplayName("Deve atualizar endereço com sucesso")
	public void deveAtualizarEnderecoComSucesso() {

		Endereco novoEndereco = new Endereco();
		novoEndereco.setRua("Rua Nova");
		novoEndereco.setBairro("Bairro Novo");

		when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
		when(enderecoRepository.save(any(Endereco.class))).thenAnswer(i -> i.getArgument(0));

		Endereco resultado = enderecoService.atualizarEndereco(1L, novoEndereco);

		assertNotNull(resultado);
		assertEquals("Rua Nova", resultado.getRua());
		assertEquals("Bairro Novo", resultado.getBairro());

		verify(enderecoRepository, times(1)).findById(1L);
		verify(enderecoRepository, times(1)).save(any(Endereco.class));
	}

	@Test
	@Order(6)
	@DisplayName("Deve lançar exceção ao atualizar endereço inexistente")
	public void deveLancarExcecaoAoAtualizarEnderecoInexistente() {

		when(enderecoRepository.findById(anyLong())).thenReturn(Optional.empty());

		Endereco novoEndereco = new Endereco();
		novoEndereco.setRua("Rua Qualquer");

		assertThrows(MsgApiException.class, () -> {
			enderecoService.atualizarEndereco(999L, novoEndereco);
		});

		verify(enderecoRepository, times(1)).findById(999L);
		verify(enderecoRepository, times(0)).save(any(Endereco.class));
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(7)
	@DisplayName("Deve deletar endereço com sucesso")
	public void deveDeletarEnderecoComSucesso() {

		when(enderecoRepository.existsById(1L)).thenReturn(true);
		doNothing().when(enderecoRepository).deleteById(1L);

		assertDoesNotThrow(() -> {
			enderecoService.deletarEndereco(1L);
		});

		verify(enderecoRepository, times(1)).existsById(1L);
		verify(enderecoRepository, times(1)).deleteById(1L);
	}

	@Test
	@Order(8)
	@DisplayName("Deve lançar exceção ao deletar endereço inexistente")
	public void deveLancarExcecaoAoDeletarEnderecoInexistente() {

		when(enderecoRepository.existsById(anyLong())).thenReturn(false);

		assertThrows(MsgApiException.class, () -> {
			enderecoService.deletarEndereco(999L);
		});

		verify(enderecoRepository, times(1)).existsById(999L);
		verify(enderecoRepository, times(0)).deleteById(anyLong());
	}
}