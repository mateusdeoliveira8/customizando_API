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

	// Busca um ProdutoJL completo pelo ID.
	// (Nome do método com "P" minúsculo em "por" — "buscarPorid" —
	// foge um pouco do padrão camelCase (o ideal seria "buscarPorId"),
	// mas não afeta o funcionamento.)
	public ProdutoJL buscarPorid(Long id) {

		// Chama um método customizado do repository chamado "buscarCompleto".
		// O nome sugere que essa query traz o produto já com seus relacionamentos
		// carregados (ex: categoria, fornecedor, itens de venda, etc.),
		// diferente de um findById() simples que só traz os dados da própria tabela.
		return produtojLRepository.buscarCompleto(id);
	}

	// Método responsável por DAR BAIXA no estoque de um produto após uma venda.
	// Recebe o ID do produto e a quantidade vendida.
	public void baixarEstoque(Long idProduto, int qtdVendida) {

		// Delega a lógica de subtração do estoque para o repository.
		// Provavelmente é uma query customizada (@Modifying + @Query) que faz
		// algo como: UPDATE produto SET estoque = estoque - qtdVendida WHERE id =
		// idProduto.
		// Não retorna nada (void) — a suposição é que a query já executa o UPDATE
		// diretamente no banco, sem precisar carregar e devolver o objeto atualizado.
		produtojLRepository.baixarEstoque(idProduto, qtdVendida);
	}

	// Método responsável por ADICIONAR estoque a um produto (ex: reposição).
	// Mesma lógica do método anterior, só que somando em vez de subtraindo.
	public void adicionarEstoque(Long idProduto, int qtdVendida) {

		// Query customizada no repository, provavelmente:
		// UPDATE produto SET estoque = estoque + qtdVendida WHERE id = idProduto.
		produtojLRepository.adicionarEstoque(idProduto, qtdVendida);
	}

	// Método que, pelo nome, DEVERIA verificar se um produto possui estoque
	// disponível.
	public void possuiEstoque(Long idProduto) {

		// Chama o método do repository, mas não faz nada com o resultado dele.
		produtojLRepository.possuiEstoque(idProduto);
	}
}