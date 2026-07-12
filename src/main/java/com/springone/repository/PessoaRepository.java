package com.springone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springone.entity.Pessoa;
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	@Query("SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
	List<Pessoa> buscarPorNome(@Param("nome") String nome);

}