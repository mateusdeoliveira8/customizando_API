package com.springone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springone.dto.CadastroDTO;
import com.springone.dto.EnderecoDTO;
import com.springone.entity.Pessoa;
import com.springone.entity.Endereco;
import com.springone.exception.MsgApiException;
import com.springone.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired

	private PessoaRepository pessoaRepository;

	public Pessoa salvarCadastro(Pessoa pessoa) {

		if (pessoa.getNome() == null || pessoa.getNome().isBlank()) {
			throw new MsgApiException("erro");
		}

		List<Pessoa> lista = pessoaRepository.findAll();

		for (Pessoa cadastroCPF : lista) {
			if (cadastroCPF.getCpf().equals(pessoa.getCpf())) {
				throw new MsgApiException("erro");
			}
		}

		return pessoaRepository.save(pessoa);
	}

	public List<Pessoa> listarCadastro() {
		return pessoaRepository.findAll();
	}

	public void deletarCadastro(Long id) {

		if (!pessoaRepository.existsById(id)) {
			throw new MsgApiException("Cadastro nao encontrado!");
		}

		pessoaRepository.deleteById(id);
	}

	public Pessoa atualizarCadastro(Long id, Pessoa novoCadastro) {

		Pessoa pessoa = pessoaRepository.findById(id).orElse(null);

		if (pessoa == null) {
			throw new MsgApiException("Cadastro nao encontrado");
		} else {
			pessoa.setNome(novoCadastro.getNome());
			pessoa.setCargo(novoCadastro.getCargo());
		}

		return pessoaRepository.save(pessoa);
	}

	public Pessoa buscarPorId(Long id) {

		Pessoa pessoa = pessoaRepository.findById(id).orElse(null);

		if (pessoa.getId() == null) {
			throw new MsgApiException("Cadastro nao encontrado");
		} else {
			return pessoa;
		}
	}

	public List<Pessoa> buscarPorNome(String nome) {

		if (nome == null || nome.isEmpty()) {
			return pessoaRepository.findAll();
		}

		return pessoaRepository.buscarPorNome(nome);
	}

	public Pessoa buscarPorCPF(String cpf) {

		List<Pessoa> pessoas = pessoaRepository.findAll();

		for (Pessoa pessoa : pessoas) {

			if (pessoa.getCpf().equals(cpf)) {
				return pessoa;
			}
		}

		throw new MsgApiException("Cadastro nao encontrado!");
	}

	public List<CadastroDTO> listaCadastro() {
		List<CadastroDTO> lista = new ArrayList<CadastroDTO>();

		for (Pessoa pessoa : listarCadastro()) {

			CadastroDTO dto = new CadastroDTO(pessoa);

			for (Endereco endereco : pessoa.getEnderecos()) {
				EnderecoDTO enderecoDTO = new EnderecoDTO();
				enderecoDTO.setBairro(endereco.getBairro());
				enderecoDTO.setCidade(endereco.getCidade());
				enderecoDTO.setNumero(endereco.getNumero());
				enderecoDTO.setRua(endereco.getRua());
				enderecoDTO.setUf(endereco.getUf());
				dto.getEnderecoDTOs().add(enderecoDTO);

			}
			lista.add(dto);

		}
		return lista;
	}
}
