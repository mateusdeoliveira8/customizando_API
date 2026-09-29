package com.springone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springone.entity.ItemProdutoVenda;

public interface RelatorioRepository extends JpaRepository<ItemProdutoVenda, Long> {

}
