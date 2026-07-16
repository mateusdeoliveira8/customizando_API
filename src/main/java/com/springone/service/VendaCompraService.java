package com.springone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springone.entity.VendaCompra;
import com.springone.repository.VendaCompraRepository;

@Service
public class VendaCompraService {

	@Autowired
	VendaCompraRepository vendaCompraRepository;

	public VendaCompra salvar(VendaCompra vendaCompra) {
		return vendaCompraRepository.save(vendaCompra);


	}

}
