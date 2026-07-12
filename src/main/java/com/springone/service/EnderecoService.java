package com.springone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springone.entity.Endereco;
import com.springone.exception.MsgApiException;
import com.springone.repository.EnderecoRepository;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository enderecoRepository;

	public Endereco salvarEndereco(Endereco Endereco) {

		return enderecoRepository.save(Endereco);
	}

	public List<Endereco> listarEndereco() {
		return enderecoRepository.findAll();
	}

	public void deletarEndereco(Long id) {

		if (!enderecoRepository.existsById(id)) {
			throw new MsgApiException("Endereco nao encontrado!");
		}

		enderecoRepository.deleteById(id);
	}

	public Endereco atualizarEndereco(Long id, Endereco novoEndereco) {

		Endereco Endereco = enderecoRepository.findById(id).orElse(null);

		if (Endereco == null) {
			throw new MsgApiException("Endereco nao encontrado");
		} else {
			Endereco.setRua(novoEndereco.getRua());
			Endereco.setBairro(novoEndereco.getBairro());
		}

		return enderecoRepository.save(Endereco);
	}

	public Endereco buscarPorId(Long id) {

		Endereco endereco = enderecoRepository.findById(id).orElse(null);

		if (endereco == null) {
			throw new MsgApiException("Endereco nao encontrado");
		} else {
			return endereco;
		}
	}

}
