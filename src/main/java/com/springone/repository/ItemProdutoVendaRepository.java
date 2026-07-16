package com.springone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springone.entity.ItemProdutoVenda;

@Repository
public interface ItemProdutoVendaRepository extends JpaRepository<ItemProdutoVenda, Long> {

}
