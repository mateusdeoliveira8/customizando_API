package com.springone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springone.exception.MsgApiException;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {

	@GetMapping("/teste")

	public ResponseEntity<String> teste(@RequestHeader("Authorization") String authorization) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			throw new MsgApiException("Token JWT não informado");
		}

		String jwt = authorization.substring(7);

		System.out.println(jwt);


		return ResponseEntity.ok("ok");

	}

}
