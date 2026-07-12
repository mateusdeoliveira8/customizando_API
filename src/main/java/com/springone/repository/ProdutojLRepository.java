package com.springone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springone.entity.ProdutoJL;

@Repository
public interface ProdutojLRepository extends JpaRepository<ProdutoJL, Long> {

	@Query("SELECT c FROM ProdutoJL c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
	ProdutoJL findByNome(String nome);
	
	@Query("select p " +
		       " from ProdutoJL p " +
			" join fetch p.categoria1 " +
			" where p.id = :id")
	ProdutoJL buscarCompleto(Long id);


}
