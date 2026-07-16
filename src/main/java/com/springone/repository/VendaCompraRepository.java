package com.springone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springone.entity.VendaCompra;

@Repository
public interface VendaCompraRepository extends JpaRepository<VendaCompra, Long> {

}
