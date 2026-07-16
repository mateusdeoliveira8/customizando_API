package com.springone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springone.entity.ProdutoJL;
import com.springone.exception.MsgApiException;
import com.springone.repository.ProdutojLRepository;

/**
 * Serviço responsável pelas regras de negócio relacionadas à entidade
 * {@link ProdutoJL}.
 *
 * <p>
 * Esta classe realiza as operações de cadastro, consulta, atualização e
 * exclusão de produtos, utilizando o repositório para persistência dos dados.
 * Também realiza validações básicas antes de executar determinadas operações.
 * </p>
 *
 * @author Mateus Oliveira
 * @since 1.0
 */
@Service
public class ProdutoJLService {

	@Autowired
	private ProdutojLRepository produtojLRepository;

	/**
	 * Salva um novo produto no banco de dados e retorna o objeto completo,
	 * incluindo seus relacionamentos.
	 *
	 * @param produto Produto que será salvo.
	 * @return Produto salvo com seus relacionamentos carregados.
	 */
	public ProdutoJL salvarProduto(ProdutoJL produto) {
		produtojLRepository.save(produto);

		ProdutoJL produtosalvo = produtojLRepository.buscarCompleto(produto.getId());
		System.out.println(produtosalvo.getCategoria1().getNome());
		return produtosalvo;
	}

	/**
	 * Salva uma lista de produtos.
	 *
	 * @param produtos Lista de produtos que serão persistidos.
	 * @return Lista contendo os produtos salvos.
	 */
	public List<ProdutoJL> salvarProdutos(List<ProdutoJL> produtos) {
		return produtojLRepository.saveAll(produtos);
	}

	/**
	 * Retorna todos os produtos cadastrados.
	 *
	 * @return Lista de produtos.
	 */
	public List<ProdutoJL> listarProdutos() {
		return produtojLRepository.findAll();
	}

	/**
	 * Atualiza os dados de um produto existente.
	 *
	 * <p>
	 * Caso o produto não seja encontrado pelo identificador informado, uma exceção
	 * do tipo {@link MsgApiException} será lançada.
	 * </p>
	 *
	 * @param id                Identificador do produto.
	 * @param produtoatualizado Objeto contendo os novos dados do produto.
	 * @return Produto atualizado.
	 * @throws MsgApiException caso o produto não exista.
	 */
	public ProdutoJL atualizarProduto(Long id, ProdutoJL produtoatualizado) {

		ProdutoJL produto = produtojLRepository.findById(id).orElse(null);

		if (produto == null) {
			throw new MsgApiException("Produto nao encontrado");

		} else {
			produto.setNome(produtoatualizado.getNome());
			produto.setPreco(produtoatualizado.getPreco());
			produto.setQuantidade(produtoatualizado.getQuantidade());

		}

		return produtojLRepository.save(produto);

	}

	/**
	 * Atualiza um produto utilizando o método saveAndFlush().
	 *
	 * <p>
	 * O método salva as alterações e sincroniza imediatamente os dados com o banco
	 * de dados.
	 * </p>
	 *
	 * @param produtoJL Produto que será atualizado.
	 * @return Produto atualizado.
	 */
	public ProdutoJL atualizar(ProdutoJL produtoJL) {
		return produtojLRepository.saveAndFlush(produtoJL);
	}

	/**
	 * Remove um produto do banco de dados.
	 *
	 * <p>
	 * Antes da exclusão, é verificado se o produto existe. Caso contrário, uma
	 * {@link MsgApiException} será lançada.
	 * </p>
	 *
	 * @param id Identificador do produto.
	 * @throws MsgApiException caso não exista produto com o id informado.
	 */
	public void deletar(Long id) {

		ProdutoJL produtoJL = produtojLRepository.findById(id).orElse(null);

		if (produtoJL == null) {
			throw new MsgApiException("Nao existe produto com esse ID");
		}

		produtojLRepository.deleteById(id);

	}

	public ProdutoJL buscarPorid(Long id) {
		return produtojLRepository.buscarCompleto(id);

	}

}