package com.springone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springone.entity.VendaCompra;
import com.springone.service.VendaCompraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendacompra")
public class ControllerVendaCompra {

	@Autowired
	VendaCompraService vendacompraservice;

	public ResponseEntity<VendaCompra> salvar(@RequestBody @Valid VendaCompra vendaCompra) {
		VendaCompra vendaCompra1 = vendacompraservice.salvar(vendaCompra);
		return ResponseEntity.ok(vendaCompra1);
	}

	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		vendacompraservice.deletar(id);
		return ResponseEntity.ok().build();
	}
}
