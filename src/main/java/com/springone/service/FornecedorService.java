package com.springone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springone.dto.FornecedorDTO;
import com.springone.entity.Endereco;
import com.springone.entity.Fornecedor;
import com.springone.exception.MsgApiException;
import com.springone.repository.FornecedorRepository;
import com.springone.repository.PessoaRepository;

@Service
public class FornecedorService {

	@Autowired
	FornecedorRepository fornecedorRepository;

	@Autowired
	PessoaRepository pessoaRepository;

	@Autowired
	RestTemplate restTemplate;

	public Fornecedor salvar(Fornecedor fornecedor) {

		FornecedorDTO dto = consultar(fornecedor.getCnpj());

		fornecedor.setNome_fantasia(dto.getNome_fantasia());
		fornecedor.setPorte(dto.getPorte());
		fornecedor.setRazao_social(dto.getRazao_social());


		Endereco endereco = new Endereco();

		endereco.setBairro(dto.getBairro());
		endereco.setNumero(Integer.parseInt(dto.getNumero()));
		endereco.setRua(dto.getLogradouro());
		endereco.setCidade(dto.getMunicipio());
		endereco.setUf(dto.getUf());
		endereco.setPessoa(pessoaRepository.buscaporCPF("07263893300"));
		fornecedor.setEndereco(endereco);


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

	public String consultar01(String cnpj) {

		String url = "https://brasilapi.com.br/api/cnpj/v1/" + cnpj.replaceAll("\\D", "");

		ResponseEntity<String> dto = restTemplate.getForEntity(url, String.class);

		return dto.getBody();

	}
	
	
	
	


}
