package repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.springone.entity.Pessoa;
import com.springone.repository.PessoaRepository;

import contexto.TestContextoSpring;

public class TestePessoaRepository extends TestContextoSpring {

	@Autowired
	private PessoaRepository pessoaRepository;

	/*
	 * ========================================================= MÉTODO AUXILIAR
	 * =========================================================
	 */

	private Pessoa criarCadastro(String nome, String idade, String cpf, String cargo) {

		Pessoa pessoa = new Pessoa();

		pessoa.setNome(nome);
		pessoa.setIdade(idade);
		pessoa.setCpf(cpf);
		pessoa.setCargo(cargo);

		return pessoa;
	}

	/*
	 * ========================================================= TESTES DE SAVE
	 * =========================================================
	 */

	@Test
	@Order(1)
	@DisplayName("Deve salvar cadastro com sucesso")
	public void deveSalvarCadastroComSucesso() {

		Pessoa pessoa = criarCadastro("Mateus", "21", "12345678901", "Desenvolvedor");

		pessoa = pessoaRepository.saveAndFlush(pessoa);

		assertNotNull(pessoa);
		assertNotNull(pessoa.getId());

		assertEquals("Mateus", pessoa.getNome());
		assertEquals("21", pessoa.getIdade());
		assertEquals("12345678901", pessoa.getCpf());
		assertEquals("Desenvolvedor", pessoa.getCargo());
	}

	/*
	 * ========================================================= TESTES DE QUERY
	 * =========================================================
	 */

	@Test
	@Order(2)
	@DisplayName("Deve buscar cadastro por nome")
	public void deveBuscarCadastroPorNome() {

		Pessoa pessoa = criarCadastro("João Teste", "30", "11111111111", "Analista");

		pessoaRepository.saveAndFlush(pessoa);

		List<Pessoa> lista = pessoaRepository.buscarPorNome("João");

		assertNotNull(lista);
		assertFalse(lista.isEmpty());

		assertEquals("João Teste", lista.get(0).getNome());
	}

	@Test
	@Order(3)
	@DisplayName("Deve buscar ignorando letras maiusculas")
	public void deveBuscarCadastroCaseInsensitive() {

		Pessoa pessoa = criarCadastro("Carlos Silva", "40", "22222222222", "Gerente");

		pessoaRepository.saveAndFlush(pessoa);

		List<Pessoa> lista = pessoaRepository.buscarPorNome("cArLoS");

		assertFalse(lista.isEmpty());

		assertEquals("Carlos Silva", lista.get(0).getNome());
	}

	@Test
	@Order(4)
	@DisplayName("Deve retornar lista vazia")
	public void deveRetornarListaVazia() {

		List<Pessoa> lista = pessoaRepository.buscarPorNome("XYZ123");

		assertNotNull(lista);
		assertTrue(lista.isEmpty());
	}

	/*
	 * ========================================================= TESTES DE UPDATE
	 * =========================================================
	 */

	@Test
	@Order(5)
	@DisplayName("Deve atualizar cadastro")
	public void deveAtualizarCadastro() {

		Pessoa pessoa = criarCadastro("Nome Antigo", "20", "33333333333", "Auxiliar");

		pessoa = pessoaRepository.saveAndFlush(pessoa);

		pessoa.setNome("Nome Novo");

		pessoaRepository.saveAndFlush(pessoa);

		Pessoa cadastroAtualizado = pessoaRepository.findById(pessoa.getId()).orElseThrow();

		assertEquals("Nome Novo", cadastroAtualizado.getNome());
	}

	/*
	 * ========================================================= TESTES DE DELETE
	 * =========================================================
	 */

	@Test
	@Order(6)
	@DisplayName("Deve excluir cadastro")
	public void deveExcluirCadastro() {

		Pessoa pessoa = criarCadastro("Cadastro Delete", "25", "44444444444", "Assistente");

		pessoa = pessoaRepository.saveAndFlush(pessoa);

		Long id = pessoa.getId();

		pessoaRepository.deleteById(id);

		assertFalse(pessoaRepository.findById(id).isPresent());
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
		pessoa.setNome("Mateus");
		pessoa.setIdade("21");
		pessoa.setCpf("12345678901");
		pessoa.setCargo("Programador");

		assertEquals(1L, pessoa.getId());
		assertEquals("Mateus", pessoa.getNome());
		assertEquals("21", pessoa.getIdade());
		assertEquals("12345678901", pessoa.getCpf());
		assertEquals("Programador", pessoa.getCargo());
	}

	/*
	 * ========================================================= TESTE FINAL
	 * =========================================================
	 */

	@Test
	@Order(8)
	@DisplayName("SaveAndFlush nao deve lançar excecao")
	public void saveAndFlushNaoDeveLancarExcecao() {

		Pessoa pessoa = criarCadastro("Cadastro Final", "22", "55555555555", "Tester");

		assertDoesNotThrow(() -> {
			pessoaRepository.saveAndFlush(pessoa);
		});
	}
}