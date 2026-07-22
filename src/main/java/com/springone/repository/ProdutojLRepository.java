package com.springone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	@Modifying
	@Query("update ProdutoJL set quantidade = quantidade - :qtdVendida  where id = :idProduto")
	void baixarEstoque(@Param("idProduto") Long idProduto, @Param("qtdVendida") int qtdVendida);

	@Query("select p.quantidade > 0 from ProdutoJL p where p.id = :idProduto")
	boolean possuiEstoque(@Param("idProduto") Long idProduto);

}
