package com.springone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springone.entity.ItemProdutoVenda;

@Repository
public interface ItemProdutoVendaRepository extends JpaRepository<ItemProdutoVenda, Long> {

	@Query("select distinct i from ItemProdutoVenda i where lower(i.produtoJL.nome) like lower(concat('%', :nome, '%'))")
	List<ItemProdutoVenda> buscarVendaPorProduto1(@Param("nome") String nome);

}
