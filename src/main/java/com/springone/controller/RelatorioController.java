package com.springone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springone.dto.ProdutoMaisVendidoDTO;
import com.springone.service.RelatorioSerivce;

@RestController
@RequestMapping("api/relatorio")
public class RelatorioController {
	
	@Autowired
	RelatorioSerivce relatorioSerivce;
	
	
	@GetMapping("/produtos-mais-vendidos")
	public ResponseEntity<List<ProdutoMaisVendidoDTO>> produtoMaisVendidtoDTO() {
		return ResponseEntity.ok(relatorioSerivce.produtoMaisVendido());

	}
	

}
