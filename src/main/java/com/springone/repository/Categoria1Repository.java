package com.springone.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springone.entity.Categoria1;

@Repository
public interface Categoria1Repository extends JpaRepository<Categoria1, Long> {

}
