package com.springone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springone.entity.Endereco;
import com.springone.exception.MsgApiException;
import com.springone.repository.EnderecoRepository;

// @Service indica ao Spring que essa classe é um "serviço":
// uma camada que concentra as REGRAS DE NEGÓCIO da aplicação.
// Isso faz o Spring criar automaticamente um objeto (bean) dessa classe
// e gerenciá-lo, permitindo que ela seja injetada em outras classes (ex: Controllers).
@Service
public class EnderecoService {

	// @Autowired diz ao Spring: "injete aqui uma instância pronta de
	// EnderecoRepository".
	// Ou seja, não precisamos fazer "new EnderecoRepository()" manualmente,
	// o Spring cuida de criar e entregar esse objeto (Injeção de Dependência).
	// EnderecoRepository é a interface responsável por acessar o banco de dados
	// (geralmente estende JpaRepository, que já traz métodos prontos como save,
	// findAll, etc.)
	@Autowired
	private EnderecoRepository enderecoRepository;

	// Método responsável por SALVAR um novo endereço no banco.
	// Recebe um objeto Endereco (obs: o parâmetro está com "E" maiúsculo,
	// o que é um erro de convenção — variáveis/parâmetros devem começar com
	// minúscula,
	// tipo "endereco". Isso não quebra o código, mas é considerado má prática em
	// Java).
	public Endereco salvarEndereco(Endereco endereco) {

		// O método save() do JpaRepository serve tanto para CRIAR quanto ATUALIZAR:
		// se o objeto não tiver ID (ou o ID não existir no banco), ele insere um novo
		// registro.
		// Ele retorna o objeto já salvo (com o ID gerado pelo banco, por exemplo).
		return enderecoRepository.save(endereco);
	}

	// Método que retorna TODOS os endereços cadastrados no banco.
	// findAll() é um método pronto do JpaRepository, já implementado por baixo dos
	// panos.
	public List<Endereco> listarEndereco() {
		return enderecoRepository.findAll();
	}

	// Método responsável por DELETAR um endereço a partir do ID.
	public void deletarEndereco(Long id) {

		// Antes de deletar, verifica se o ID realmente existe no banco.
		// existsById() retorna um boolean (true/false).
		// Se NÃO existir (por isso o "!" de negação), lança uma exceção customizada.
		if (!enderecoRepository.existsById(id)) {
			throw new MsgApiException("Endereco nao encontrado!");
		}

		// Se passou pela validação acima, significa que o ID existe,
		// então pode deletar com segurança.
		enderecoRepository.deleteById(id);
	}

	// Método responsável por ATUALIZAR um endereço já existente.
	// Recebe o ID do endereço que será atualizado e um objeto "novoEndereco"
	// contendo os novos dados.
	public Endereco atualizarEndereco(Long id, Endereco novoEndereco) {

		// Busca o endereço atual no banco pelo ID.
		// findById() retorna um Optional<Endereco> — um "envelope" que pode conter
		// o objeto ou estar vazio (evita o uso direto de null e NullPointerException).
		// .orElse(null) diz: "se não encontrar nada, retorne null em vez do Optional".
		Endereco Endereco = enderecoRepository.findById(id).orElse(null);

		// Se não encontrou o endereço (deu null), lança exceção.
		if (Endereco == null) {
			throw new MsgApiException("Endereco nao encontrado");
		} else {
			// Se encontrou, atualiza apenas os campos "rua" e "bairro"
			// com os valores vindos de "novoEndereco".
			// Ou seja, essa atualização é PARCIAL: só esses dois campos são alterados,
			// outros atributos da entidade (se existirem, tipo cidade, CEP, etc.)
			// permanecem como estavam antes.
			Endereco.setRua(novoEndereco.getRua());
			Endereco.setBairro(novoEndereco.getBairro());
		}

		// Salva o objeto já atualizado no banco (aqui o save() faz um UPDATE,
		// pois o objeto já possui um ID existente).
		return enderecoRepository.save(Endereco);
	}

	// Método responsável por BUSCAR um único endereço pelo ID.
	public Endereco buscarPorId(Long id) {

		// Mesma lógica do método anterior: tenta buscar, se não achar retorna null.
		Endereco endereco = enderecoRepository.findById(id).orElse(null);

		// Se não encontrou, lança exceção.
		if (endereco == null) {
			throw new MsgApiException("Endereco nao encontrado");
		} else {
			// Se encontrou, retorna o endereço encontrado.
			return endereco;
		}
	}

	public Page<Endereco> listaPaginada(int page, int size) {
		return enderecoRepository.findAll(PageRequest.of(page, size, Sort.by("rua")));

	}

}