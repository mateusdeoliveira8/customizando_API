package service; // Define o pacote onde esta classe de teste está localizada.

import static org.junit.jupiter.api.Assertions.assertEquals; // Importa o método assertEquals, utilizado para comparar o resultado esperado com o resultado obtido durante o teste.

import java.util.Calendar; // Importa a classe Calendar para obter a data e hora atual.

import org.junit.jupiter.api.Test; // Indica que o método será executado como um teste unitário.
import org.junit.jupiter.api.extension.ExtendWith; // Permite utilizar extensões do JUnit.
import org.mockito.junit.jupiter.MockitoExtension; // Habilita o uso do Mockito durante a execução dos testes.
import org.springframework.beans.factory.annotation.Autowired; // Injeta automaticamente as dependências gerenciadas pelo Spring.

import com.springone.entity.ItemProdutoVenda; // Entidade que representa um item pertencente a uma venda.
import com.springone.entity.Pessoa; // Entidade que representa um cliente.
import com.springone.entity.ProdutoJL; // Entidade que representa um produto.
import com.springone.entity.VendaCompra; // Entidade que representa a venda.
import com.springone.service.ItemProdutoVendaService; // Serviço responsável pelas operações dos itens da venda.
import com.springone.service.PessoaService; // Serviço responsável pelas operações da entidade Pessoa.
import com.springone.service.ProdutoJLService; // Serviço responsável pelas operações da entidade Produto.
import com.springone.service.VendaCompraService; // Serviço responsável pelas operações da entidade Venda.

import contexto.TestContextoSpring; // Classe responsável por carregar o contexto do Spring para os testes.

@ExtendWith(MockitoExtension.class) // Ativa o suporte ao Mockito durante a execução dos testes.
public class TesteVendaCompraService extends TestContextoSpring { // Classe responsável por testar o fluxo completo de
																	// uma venda.

	@Autowired
	private PessoaService pessoaService; // Injeta o serviço responsável pelo cliente.

	@Autowired
	private VendaCompraService vendaCompraService; // Injeta o serviço responsável pela venda.

	@Autowired
	private ProdutoJLService produtoJLService; // Injeta o serviço responsável pelos produtos.

	@Autowired
	ItemProdutoVendaService itemProdutoVendaService; // Injeta o serviço responsável pelos itens da venda.

	@Test // Define que este método será executado como teste.
	public void teste() {

		System.out.println("Testando teste"); // Apenas imprime uma mensagem para indicar que o teste iniciou.

		// Busca o produto de ID 1 no banco de dados.
		ProdutoJL produto01 = produtoJLService.buscarPorid(1L);

		// Define que este produto possui 10 unidades em estoque.
		produto01.setQuantidade(10);

		// Salva a alteração do estoque no banco.
		produto01 = produtoJLService.salvarProduto(produto01);

		// Busca o produto de ID 2.
		ProdutoJL produto02 = produtoJLService.buscarPorid(2L);

		// Define que o segundo produto também possui 10 unidades.
		produto02.setQuantidade(10);

		// Salva a alteração no banco.
		produto02 = produtoJLService.salvarProduto(produto02);

		// Busca o cliente utilizando o CPF informado.
		Pessoa pessoa = pessoaService.buscarPorCPF("123456789100");

		// Cria uma nova venda.
		VendaCompra vendaCompra = new VendaCompra();

		// Associa o cliente à venda.
		vendaCompra.setPessoa(pessoa);

		// Define a data atual como data da venda.
		vendaCompra.setData(Calendar.getInstance().getTime());

		// Salva a venda para que ela seja registrada no banco de dados.
		vendaCompra = vendaCompraService.salvar(vendaCompra);

		// Cria o primeiro item da venda.
		ItemProdutoVenda itemProdutoVenda = new ItemProdutoVenda();

		// Define qual produto será vendido.
		itemProdutoVenda.setProdutoJL(produto01);

		// Define um desconto de R$ 5,00 para este item.
		itemProdutoVenda.setDesconto(5);

		// Define que serão vendidas duas unidades do produto.
		itemProdutoVenda.setQuantidade(2);

		// Calcula automaticamente o valor final deste item considerando quantidade e
		// desconto.
		itemProdutoVenda.calcularValor();

		// Relaciona o item à venda criada anteriormente.
		itemProdutoVenda.setVendaCompra(vendaCompra);

		// Verifica se existe quantidade suficiente em estoque antes de concluir a
		// venda.
		if (produto01.getQuantidade() >= itemProdutoVenda.getQuantidade()) {

			// Salva o item da venda no banco.
			itemProdutoVenda = itemProdutoVendaService.salvar(itemProdutoVenda);

			// Adiciona o item à lista de itens da venda.
			vendaCompra.getItensProdutos().add(itemProdutoVenda);
		}

		// Cria o segundo item da venda.
		ItemProdutoVenda itemProdutoVenda2 = new ItemProdutoVenda();

		// Define o segundo produto.
		itemProdutoVenda2.setProdutoJL(produto02);

		// Define desconto de R$ 2,00.
		itemProdutoVenda2.setDesconto(2);

		// Define a quantidade comprada.
		itemProdutoVenda2.setQuantidade(2);

		// Calcula o valor deste item.
		itemProdutoVenda2.calcularValor();

		// Relaciona este item com a venda.
		itemProdutoVenda2.setVendaCompra(vendaCompra);

		// Confirma se há estoque disponível.
		if (produto02.getQuantidade() >= itemProdutoVenda2.getQuantidade()) {

			// Salva o item.
			itemProdutoVenda2 = itemProdutoVendaService.salvar(itemProdutoVenda2);

			// Adiciona o item à lista de itens da venda.
			vendaCompra.getItensProdutos().add(itemProdutoVenda2);
		}

		// Soma todos os descontos dos itens para obter o desconto total da venda.
		vendaCompra.calcularDesconto();

		// Calcula o valor total da venda somando todos os itens.
		vendaCompra.calcularValor();

		// Atualiza a venda com os valores calculados.
		vendaCompra = vendaCompraService.salvar(vendaCompra);

		// Exibe o código da venda gerado automaticamente.
		System.out.println("Codigo da Venda : " + vendaCompra.getId());

		// Percorre todos os itens vendidos.
		for (ItemProdutoVenda item : vendaCompra.getItensProdutos()) {

			// Para cada item vendido, reduz a quantidade correspondente do estoque.
			produtoJLService.baixarEstoque(item.getProdutoJL().getId(), item.getQuantidade());
		}

		// Busca novamente os produtos para verificar se o estoque foi atualizado
		// corretamente.
		produto01 = produtoJLService.buscarPorid(1L);
		produto02 = produtoJLService.buscarPorid(1L);

		// Verifica se o produto 1 ficou com 8 unidades em estoque.
		assertEquals(8, produto01.getQuantidade());

		// Verifica se o produto 2 ficou com 8 unidades em estoque.
		assertEquals(8, produto02.getQuantidade());

		// Confirma se o valor total da venda foi calculado corretamente.
		assertEquals(9993.00, vendaCompra.getTotalVendaFinal());

		// Confirma se a soma dos descontos foi de R$ 7,00.
		assertEquals(7, vendaCompra.getDesconto());

		// Confirma se o valor do primeiro item foi calculado corretamente.
		assertEquals(4995.00, itemProdutoVenda.getValor());

		// Confirma se o valor do segundo item foi calculado corretamente.
		assertEquals(4998.00, itemProdutoVenda2.getValor());

	}
}