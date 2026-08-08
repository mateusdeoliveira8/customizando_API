package com.springone.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springone.entity.VendaCompra;

@Repository
public interface VendaCompraRepository extends JpaRepository<VendaCompra, Long> {

	@Query("select v from VendaCompra v join v.pessoa  p where lower(p.nome) = :nome")
	List<VendaCompra> buscarVendaPorNomeDoCliente(@Param("nome") String nome);
	
	@Query("select v from VendaCompra v where cast(v.data as date)  = :data ")
	List<VendaCompra> buscarVendaPorData(@Param("data") Date data);

	@Query("select distinct i.vendaCompra from ItemProdutoVenda i where lower(i.produtoJL.nome) like lower(concat('%', :nome, '%'))")
	List<VendaCompra> buscarVendaPorProduto(@Param("nome") String nome);

}