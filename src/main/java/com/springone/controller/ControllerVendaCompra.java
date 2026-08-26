package com.springone.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springone.dto.VendaCompraDTO;
import com.springone.entity.VendaCompra;
import com.springone.service.VendaCompraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendacompra")
public class ControllerVendaCompra {

	@Autowired
	VendaCompraService vendacompraservice;

	@GetMapping("/teste")
	public String teste() {
		return "Controller funcionando";
	}

	@PostMapping("/salvar")
	public ResponseEntity<VendaCompra> salvar(@RequestBody @Valid VendaCompra vendacompra) {
		VendaCompra vendaCompra1 = vendacompraservice.salvar(vendacompra);
		return ResponseEntity.ok(vendaCompra1);
	}

	@DeleteMapping("deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		vendacompraservice.deletar(id);
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<VendaCompra> atualizar(VendaCompra vendaCompra) {
		return ResponseEntity.ok(vendacompraservice.ataulzar(vendaCompra));

	}


	@GetMapping("/buscar")
	public ResponseEntity<List<VendaCompraDTO>> buscarVendaPorNomeDoClienteDTO(@RequestParam String nome) {
		return ResponseEntity.ok(vendacompraservice.buscarVendaPorNomeDoClienteDTO(nome));
	}

	@GetMapping("/buscarpordata")
	public ResponseEntity<List<VendaCompraDTO>> buscarPorData02(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date data) {
		return ResponseEntity.ok(vendacompraservice.buscarPorData02(data));

	}

	public ResponseEntity<List<VendaCompra>> buscarVendaPorData(@RequestParam Date date) {
		return ResponseEntity.ok(vendacompraservice.buscarVendaPorData(date));
	}

}
