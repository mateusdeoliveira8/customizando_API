package com.springone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springone.dto.FornecedorDTO;
import com.springone.entity.Fornecedor;
import com.springone.exception.MsgApiException;
import com.springone.repository.FornecedorRepository;

@Service
public class FornecedorService {

	@Autowired
	FornecedorRepository fornecedorRepository;

	@Autowired
	RestTemplate restTemplate;

	public Fornecedor salvar(Fornecedor fornecedor) {

		FornecedorDTO dto = consultar(fornecedor.getCnpj());

		fornecedor.setNome_fantasia(dto.getNome_fantasia());
		fornecedor.setPorte(dto.getPorte());
		fornecedor.setRazao_social(dto.getRazao_social());

		return fornecedorRepository.save(fornecedor);

	}

	public List<Fornecedor> listar() {

		return fornecedorRepository.findAll();

	}

	public Fornecedor atualizar(Long id, Fornecedor fornecedor) {

		Fornecedor fornecedor1 = fornecedorRepository.findById(id).orElse(null);

		if (fornecedor1 == null) {

			throw new MsgApiException("Fornecedor não encontrado");
		}

		fornecedor.setId(fornecedor1.getId());

		return fornecedorRepository.save(fornecedor);

	}



	public void deletar(Long id) {

		if (!fornecedorRepository.existsById(id)) {

			throw new MsgApiException("Fornecedor não encontrado");

		}

		fornecedorRepository.deleteById(id);
	}

	public FornecedorDTO consultar(String cnpj) {

		String url = "https://brasilapi.com.br/api/cnpj/v1/" + cnpj.replaceAll("\\D", "");

		ResponseEntity<FornecedorDTO> dto = restTemplate.getForEntity(url, FornecedorDTO.class);

		return dto.getBody();

	}

}
