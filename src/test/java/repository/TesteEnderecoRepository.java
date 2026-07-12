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

import com.springone.entity.Endereco;
import com.springone.entity.Pessoa;
import com.springone.repository.EnderecoRepository;
import com.springone.repository.PessoaRepository;

import contexto.TestContextoSpring;

public class TesteEnderecoRepository extends TestContextoSpring {

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	/*
	 * ========================================================= MÉTODO AUXILIAR
	 * =========================================================
	 */

	private Pessoa criarPessoa() {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Pessoa Teste");
		pessoa.setIdade("30");
		pessoa.setCpf("99999999999");
		pessoa.setCargo("Tester");
		return pessoaRepository.saveAndFlush(pessoa);
	}

	private Endereco criarEndereco(String rua, int numero, String bairro, String cidade, String uf, Pessoa pessoa) {
		Endereco endereco = new Endereco();
		endereco.setRua(rua);
		endereco.setNumero(numero);
		endereco.setBairro(bairro);
		endereco.setCidade(cidade);
		endereco.setUf(uf);
		endereco.setPessoa(pessoa);
		return endereco;
	}

	/*
	 * ========================================================= TESTES DE SAVE
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("Deve salvar endereço com sucesso")
	public void deveSalvarEnderecoComSucesso() {

		Pessoa pessoa = criarPessoa();

		Endereco endereco = criarEndereco("Rua das Flores", 100, "Centro", "São Paulo", "SP", pessoa);

		endereco = enderecoRepository.saveAndFlush(endereco);

		assertNotNull(endereco);
		assertNotNull(endereco.getId());

		assertEquals("Rua das Flores", endereco.getRua());
		assertEquals(100, endereco.getNumero());
		assertEquals("Centro", endereco.getBairro());
		assertEquals("São Paulo", endereco.getCidade());
		assertEquals("SP", endereco.getUf());
		assertNotNull(endereco.getPessoa());
	}

	/*
	 * ========================================================= TESTES DE QUERY
	 * =========================================================
	 */

	@Test
	@Order(2)
	@DisplayName("Deve buscar todos os endereços")
	public void deveBuscarTodosOsEnderecos() {

		Pessoa pessoa = criarPessoa();

		Endereco endereco = criarEndereco("Rua das Palmeiras", 200, "Jardim", "Campinas", "SP", pessoa);

		enderecoRepository.saveAndFlush(endereco);

		List<Endereco> lista = enderecoRepository.findAll();

		assertNotNull(lista);
		assertFalse(lista.isEmpty());
	}

	@Test
	@Order(3)
	@DisplayName("Deve buscar endereço por ID")
	public void deveBuscarEnderecoPorId() {

		Pessoa pessoa = criarPessoa();

		Endereco endereco = criarEndereco("Av. Brasil", 300, "Vila Nova", "Rio de Janeiro", "RJ", pessoa);

		endereco = enderecoRepository.saveAndFlush(endereco);

		Optional<Endereco> encontrado = enderecoRepository.findById(endereco.getId());

		assertTrue(encontrado.isPresent());
		assertEquals("Av. Brasil", encontrado.get().getRua());
		assertEquals(300, encontrado.get().getNumero());
	}

	@Test
	@Order(4)
	@DisplayName("Deve retornar vazio para ID inexistente")
	public void deveRetornarVazioParaIdInexistente() {

		Optional<Endereco> encontrado = enderecoRepository.findById(999999L);

		assertFalse(encontrado.isPresent());
	}

	/*
	 * ========================================================= TESTES DE UPDATE
	 * =========================================================
	 */

	@Test
	@Order(5)
	@DisplayName("Deve atualizar endereço")
	public void deveAtualizarEndereco() {

		Pessoa pessoa = criarPessoa();

		Endereco endereco = criarEndereco("Rua Antiga", 10, "Bairro Velho", "Porto Alegre", "RS", pessoa);

		endereco = enderecoRepository.saveAndFlush(endereco);

		endereco.setRua("Rua Nova");
		endereco.setNumero(999);

		enderecoRepository.saveAndFlush(endereco);

		Endereco enderecoAtualizado = enderecoRepository.findById(endereco.getId()).orElseThrow();

		assertEquals("Rua Nova", enderecoAtualizado.getRua());
		assertEquals(999, enderecoAtualizado.getNumero());
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(6)
	@DisplayName("Deve excluir endereço")
	public void deveExcluirEndereco() {

		Pessoa pessoa = criarPessoa();

		Endereco endereco = criarEndereco("Rua Delete", 50, "Bairro Teste", "Curitiba", "PR", pessoa);

		endereco = enderecoRepository.saveAndFlush(endereco);

		Long id = endereco.getId();

		enderecoRepository.deleteById(id);

		assertFalse(enderecoRepository.findById(id).isPresent());
	}

	/*
	 * ========================================================= TESTES ENTITY
	 * =========================================================
	 */

	@Test
	@Order(7)
	@DisplayName("Getters e Setters devem funcionar")
	public void gettersESettersDevemFuncionar() {

		Pessoa pessoa = new Pessoa();
		pessoa.setId(1L);

		Endereco endereco = new Endereco();

		endereco.setId(1L);
		endereco.setRua("Rua Teste");
		endereco.setNumero(42);
		endereco.setBairro("Bairro Teste");
		endereco.setCidade("Cidade Teste");
		endereco.setUf("TS");
		endereco.setPessoa(pessoa);

		assertEquals(1L, endereco.getId());
		assertEquals("Rua Teste", endereco.getRua());
		assertEquals(42, endereco.getNumero());
		assertEquals("Bairro Teste", endereco.getBairro());
		assertEquals("Cidade Teste", endereco.getCidade());
		assertEquals("TS", endereco.getUf());
		assertNotNull(endereco.getPessoa());
	}

	/*
	 * ========================================================= TESTE FINAL
	 * =========================================================
	 */

	@Test
	@Order(8)
	@DisplayName("SaveAndFlush não deve lançar exceção")
	public void saveAndFlushNaoDeveLancarExcecao() {

		Pessoa pessoa = criarPessoa();

		Endereco endereco = criarEndereco("Rua Final", 77, "Bairro Final", "Manaus", "AM", pessoa);

		assertDoesNotThrow(() -> {
			enderecoRepository.saveAndFlush(endereco);
		});
	}
}