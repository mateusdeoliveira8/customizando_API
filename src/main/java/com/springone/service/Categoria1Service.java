package com.springone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springone.entity.Categoria1;
import com.springone.exception.MsgApiException;
import com.springone.repository.Categoria1Repository;

@Service
public class Categoria1Service {

	@Autowired
	private Categoria1Repository categoria1Repository;

	public Categoria1 salvarcategoria(Categoria1 categoria1) {

		return categoria1Repository.save(categoria1);

	}

	public List<Categoria1> salvarCategorias(List<Categoria1> categoria1s) {
		return categoria1Repository.saveAll(categoria1s);
	}

	public List<Categoria1> listarCategorias() {
		return categoria1Repository.findAll();
	}

	public Categoria1 atualizarCategoria(Long id, Categoria1 categoriaAtualizado) {

		Categoria1 categoria1 = categoria1Repository.findById(id).orElse(null);

		if (categoria1 == null) {
			throw new MsgApiException("Categoria não encontrada");
		}

		categoria1.setNome(categoriaAtualizado.getNome());

		return categoria1Repository.save(categoria1);

	}

	public Categoria1 atualizar(Categoria1 categoria1) {
		return categoria1Repository.saveAndFlush(categoria1);
	}

	public void deletar(Long id) {

		Categoria1 categoria1 = categoria1Repository.findById(id).orElse(null);

		if (categoria1 == null) {
			throw new MsgApiException("Não existe categoria com esse ID");
		}

		categoria1Repository.deleteById(id);

	}
}