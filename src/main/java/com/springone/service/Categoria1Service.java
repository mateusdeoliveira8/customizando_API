package com.springone.service; // Define que esta classe pertence ao pacote "service".

// O pacote service é responsável por conter as regras de negócio da aplicação.

import java.util.List; // Importa a interface List, utilizada para trabalhar com listas de objetos.

import org.springframework.beans.factory.annotation.Autowired; // Permite ao Spring realizar a injeção automática de dependências.
import org.springframework.stereotype.Service; // Indica ao Spring que esta classe é um Service.

import com.springone.entity.Categoria1; // Importa a entidade Categoria1.
import com.springone.exception.MsgApiException; // Importa a exceção personalizada da aplicação.
import com.springone.repository.Categoria1Repository; // Importa o Repository responsável pelo acesso ao banco.

@Service // Registra esta classe como um componente Service no Spring.
public class Categoria1Service {

	// Injeta automaticamente uma instância do Categoria1Repository.
	// Assim, não é necessário criar o objeto manualmente utilizando "new".
	@Autowired
	private Categoria1Repository categoria1Repository;

	/**
	 * Salva uma nova categoria no banco de dados.
	 *
	 * @param categoria1 Objeto contendo os dados da categoria.
	 * @return Categoria salva já persistida no banco.
	 */
	public Categoria1 salvarcategoria(Categoria1 categoria1) {

		// O método save() verifica se o objeto possui ID.
		// Caso não possua, realiza um INSERT.
		// Caso possua, realiza um UPDATE.
		return categoria1Repository.save(categoria1);

	}

	/**
	 * Salva várias categorias de uma única vez.
	 *
	 * @param categoria1s Lista contendo as categorias.
	 * @return Lista de categorias persistidas.
	 */
	public List<Categoria1> salvarCategorias(List<Categoria1> categoria1s) {

		// saveAll() percorre toda a lista realizando a persistência
		// de cada objeto no banco de dados.
		return categoria1Repository.saveAll(categoria1s);
	}

	/**
	 * Lista todas as categorias cadastradas.
	 *
	 * @return Lista contendo todas as categorias.
	 */
	public List<Categoria1> listarCategorias() {

		// findAll() executa um SELECT retornando todos os registros
		// da tabela correspondente à entidade Categoria1.
		return categoria1Repository.findAll();
	}

	/**
	 * Atualiza uma categoria existente.
	 *
	 * @param id                  Identificador da categoria.
	 * @param categoriaAtualizado Objeto contendo os novos dados.
	 * @return Categoria atualizada.
	 */
	public Categoria1 atualizarCategoria(Long id, Categoria1 categoriaAtualizado) {

		// Procura uma categoria pelo ID.
		// findById() retorna um Optional.
		// Caso não encontre nenhum registro, orElse(null) retorna null.
		Categoria1 categoria1 = categoria1Repository.findById(id).orElse(null);

		// Verifica se a categoria foi encontrada.
		if (categoria1 == null) {

			// Caso não exista, lança uma exceção personalizada.
			throw new MsgApiException("Categoria não encontrada");
		}

		// Atualiza apenas o atributo nome.
		// O ID permanece o mesmo.
		categoria1.setNome(categoriaAtualizado.getNome());

		// Como o objeto já possui ID, o método save()
		// executará um UPDATE no banco de dados.
		return categoria1Repository.save(categoria1);

	}

	/**
	 * Atualiza uma categoria utilizando saveAndFlush().
	 *
	 * @param categoria1 Categoria que será atualizada.
	 * @return Categoria atualizada.
	 */
	public Categoria1 atualizar(Categoria1 categoria1) {

		// saveAndFlush() salva a alteração e força
		// imediatamente a sincronização com o banco.
		// Diferentemente do save(), que pode aguardar
		// o término da transação para enviar os dados.
		return categoria1Repository.saveAndFlush(categoria1);
	}

	/**
	 * Remove uma categoria do banco de dados.
	 *
	 * @param id Identificador da categoria.
	 */
	public void deletar(Long id) {

		// Procura a categoria antes de realizar a exclusão.
		Categoria1 categoria1 = categoria1Repository.findById(id).orElse(null);

		// Caso não exista nenhuma categoria com esse ID,
		// interrompe a operação lançando uma exceção.
		if (categoria1 == null) {
			throw new MsgApiException("Não existe categoria com esse ID");
		}

		// Remove a categoria do banco de dados utilizando o ID.
		categoria1Repository.deleteById(id);

	}
}