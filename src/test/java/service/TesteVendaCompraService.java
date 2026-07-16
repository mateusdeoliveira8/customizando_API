package service;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.springone.entity.ItemProdutoVenda;
import com.springone.entity.Pessoa;
import com.springone.entity.ProdutoJL;
import com.springone.entity.VendaCompra;
import com.springone.service.ItemProdutoVendaService;
import com.springone.service.PessoaService;
import com.springone.service.ProdutoJLService;
import com.springone.service.VendaCompraService;

import contexto.TestContextoSpring;

@ExtendWith(MockitoExtension.class)
public class TesteVendaCompraService extends TestContextoSpring {
	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private VendaCompraService vendaCompraService;

	@Autowired
	private ProdutoJLService produtoJLService;

	@Autowired
	ItemProdutoVendaService itemProdutoVendaService;


	@Test
	public void teste() {
		System.out.println("Testando teste");

		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Mateus");
		pessoa.setIdade("21");
		pessoa.setCpf("123456789700");
		pessoa.setCargo("adm");
		
		pessoa = pessoaService.salvarCadastro(pessoa);

		VendaCompra vendaCompra = new VendaCompra();
		vendaCompra.setPessoa(pessoa);
		vendaCompra.setData(Calendar.getInstance().getTime());
		vendaCompra = vendaCompraService.salvar(vendaCompra);


		ProdutoJL produto01 = produtoJLService.buscarPorid(1L);
		ItemProdutoVenda itemProdutoVenda = new ItemProdutoVenda();
		itemProdutoVenda.setProdutoJL(produto01);
		itemProdutoVenda.setDesconto(5);
		itemProdutoVenda.setQuantidade(14);
		itemProdutoVenda.calcularValor();
		itemProdutoVenda.setVendaCompra(vendaCompra);
		itemProdutoVenda = itemProdutoVendaService.salvar(itemProdutoVenda);

		ProdutoJL produto02 = produtoJLService.buscarPorid(2L);
		ItemProdutoVenda itemProdutoVenda2 = new ItemProdutoVenda();
		itemProdutoVenda2.setProdutoJL(produto02);
		itemProdutoVenda2.setDesconto(2);
		itemProdutoVenda2.setQuantidade(8);
		itemProdutoVenda2.calcularValor();
		itemProdutoVenda2.setVendaCompra(vendaCompra);
		itemProdutoVenda2 = itemProdutoVendaService.salvar(itemProdutoVenda2);

		vendaCompra.getItensProdutos().add(itemProdutoVenda);
		vendaCompra.getItensProdutos().add(itemProdutoVenda2);
		vendaCompra.calcularDesconto();
		vendaCompra.calcularValor();
		vendaCompra = vendaCompraService.salvar(vendaCompra);
		System.out.println("Codigo da Venda : " + vendaCompra.getId());

	}

}
