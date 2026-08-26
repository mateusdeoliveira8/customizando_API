package com.springone.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springone.dto.VendaCompraDTO;
import com.springone.entity.ItemProdutoVenda;
import com.springone.entity.VendaCompra;
import com.springone.exception.MsgApiException;
import com.springone.repository.VendaCompraRepository;

// Marca a classe como um Service do Spring (camada de regras de negócio).
@Service
public class VendaCompraService {

	@Autowired
	// Injeção de dependência do repository responsável por acessar
	// a entidade VendaCompra no banco de dados.
	VendaCompraRepository vendaCompraRepository;

	public VendaCompra prepersiste(VendaCompra vendaCompra) {
		return vendaCompraRepository.save(vendaCompra);
	}

	// Método responsável por SALVAR uma venda/compra, com várias validações
	// de negócio antes de persistir no banco.
	public VendaCompra salvar(VendaCompra vendaCompra) {

		// Validação 1: o valor total da venda não pode ser zero ou negativo.
		if (vendaCompra.getTotalVendaFinal() <= 0) {
			throw new MsgApiException("Valor não pode ser negativo");
		}

		// Validação 2: a venda precisa ter pelo menos um item de produto.
		// isEmpty() aqui é chamado numa List (getItensProdutos()), verificando
		// se a lista não possui nenhum elemento.
		if (vendaCompra.getItensProdutos().isEmpty()) {
			throw new MsgApiException("não pode ser vazia a lista de produtos");
		}

		// Validação 3: toda venda precisa estar associada a uma pessoa (cliente).
		if (vendaCompra.getPessoa() == null) {
			throw new MsgApiException("Venda tem que ter pessoa associada");
		}

		// Validação 4: o desconto total da venda não pode ser maior que
		// o valor total da venda (não faz sentido dar desconto maior que o preço).
		if (vendaCompra.getDesconto() > vendaCompra.getTotalVendaFinal()) {
			throw new MsgApiException("desconto nao pode ser maior que o valor da venda ");
		}

		// Agora valida cada ITEM individualmente dentro da venda.
		// Percorre a lista de itens de produto (relacionamento da venda com os produtos
		// vendidos).
		for (ItemProdutoVenda itens : vendaCompra.getItensProdutos()) {

			// Validação 5: cada item precisa ter quantidade maior que zero.
			// Não faz sentido vender "0 unidades" de um produto.
			if (itens.getQuantidade() <= 0) {
				// Monta uma mensagem de erro dinâmica, incluindo o nome do produto
				// problemático — ajuda a identificar qual item específico causou o erro.
				throw new MsgApiException(
						"produto : " + itens.getProdutoJL().getNome() + " nao pode ter a quantidade de zero na venda");
			}

			// Validação 6: o desconto do item não pode ser maior que o valor do próprio
			// item
			// (mesma lógica da validação 4, só que agora no nível de cada produto).
			if (itens.getDesconto() > itens.getValor()) {
				throw new MsgApiException("produto : " + itens.getProdutoJL().getNome()
						+ " nao pode ter desconto doque o seu valor de venda");
			}
		}

		// Se passou por todas as validações, salva a venda no banco.
		return vendaCompraRepository.save(vendaCompra);
	}

	// Busca vendas filtrando pelo nome do cliente associado.
	public List<VendaCompra> buscarVendaPorNomeDoCliente(String nome) {

		// Validação: o nome não pode ser vazio.
		// ATENÇÃO: se "nome" vier como null (e não apenas ""), essa linha vai
		// lançar um NullPointerException antes mesmo de chegar na MsgApiException,
		// porque .isEmpty() está sendo chamado diretamente sem checar null antes.
		// O ideal seria: if (nome == null || nome.isEmpty())
		if (nome.isEmpty()) {
			throw new MsgApiException("Adicione um nome para realizar a busca.");
		}

		// Chama método customizado do repository (provavelmente uma @Query
		// com LIKE, já que estamos buscando por nome do cliente, um campo
		// que pertence à entidade Pessoa relacionada à venda).
		return vendaCompraRepository.buscarVendaPorNomeDoCliente(nome);
	}

	// Busca vendas realizadas em uma determinada data.
	// Retorna um Optional<VendaCompra> — um "envelope" que pode conter
	// o resultado ou estar vazio, evitando retornar null diretamente.
	public List<VendaCompra> buscarVendaPorData(Date data) {
		return vendaCompraRepository.buscarVendaPorData(data);
	}

	public List<VendaCompra> buscarVendaPorProduto(String nome) {
		return vendaCompraRepository.buscarVendaPorProduto(nome);

	}

	public void deletar(Long id) {
		vendaCompraRepository.deleteById(id);
	}

	public VendaCompra ataulzar(VendaCompra vendaCompra) {
		return vendaCompraRepository.saveAndFlush(vendaCompra);
	}

	/*
	 * public List<VendaCompraDTO> buscarVendaPorNomeDoClienteDTO(String nome) {
	 * 
	 * List<VendaCompra> vendas =
	 * vendaCompraRepository.buscarVendaPorNomeDoCliente(nome);
	 * 
	 * List<VendaCompraDTO> lista = new ArrayList<>();
	 * 
	 * for (VendaCompra venda : vendas) {
	 * 
	 * VendaCompraDTO dto = new VendaCompraDTO(venda);
	 * 
	 * lista.add(dto); }
	 * 
	 * return lista; }
	 */

	public List<VendaCompraDTO> buscarVendaPorNomeDoClienteDTO(String nome) {

		// "A variável vendas recebe o resultado do método que busca as vendas pelo nome
		// do cliente."
		List<VendaCompra> vendas = vendaCompraRepository.buscarVendaPorNomeDoCliente(nome);

		// cria uma lista vazia para armazenar os dtos
		List<VendaCompraDTO> lista = new ArrayList<>();

		// percorre lista vendas e adicoana na variavel
		for (VendaCompra venda : vendas) {

			// cria um ibjeto dto
			VendaCompraDTO dto = new VendaCompraDTO();
			// "Aqui eu chamo os métodos do DTO, passando a venda atual para ele preencher
			// as informações."
			dto.preecnherItens(venda);
			dto.preencherVenda(venda);

			lista.add(dto);
		}

		return lista;

	}

	public List<VendaCompraDTO> buscarPorData02(Date data) {

		// variavel listadata recebe o valor do metodo buscavendapordata
		List<VendaCompra> listaData = vendaCompraRepository.buscarVendaPorData(data);

		// cria uma lista vazia chama dataDtos
		List<VendaCompraDTO> dataDtos = new ArrayList<>();

		// percorre a listaData e guarda na variavel vendaLista
		for (VendaCompra venda : listaData ) {

			// criado um novo objeto chamado listaDto
			VendaCompraDTO dto = new VendaCompraDTO();

			dto.buscarPorData02(venda);

			dataDtos.add(dto);
		}
		return dataDtos;

	}

}
