package com.springone.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PSQLException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springone.Util.ExeptionUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/* Para erro que não estamos esperando RuntimeException*/
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> erroGeralRuntime(RuntimeException ex) {
		// String erroString = ExeptionUtil.getMensagemLimpa(ex);
		String erroString1 = ExeptionUtil.extrairMensagem(ex);
		System.out.println(erroString1);
		ex.printStackTrace();

		return ResponseEntity.internalServerError()
				               .contentType(MediaType.APPLICATION_JSON)
				.body(erroString1);
	}

	@ExceptionHandler(PSQLException.class)
	public ResponseEntity<String> erroGeralPlsql(PSQLException ex) {

		ex.printStackTrace();

		return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
	}

	/* Para erro que não estamos esperando Exception*/
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> erroGeralException(Exception ex) {
		
		ex.printStackTrace();

		return ResponseEntity.internalServerError().
				             contentType(MediaType.APPLICATION_JSON)
				             .body(ex.getMessage());
	}
	
	
	/* Captura erro do Bean Validator */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

		List<String> lista = new ArrayList<String>();

		for (ObjectError erro : ex.getAllErrors()) {
			lista.add(erro.getDefaultMessage());
		}

		return ResponseEntity.ok().body(lista);
	}
	
	
	/* Para erro que não estamos esperando MsgApiException*/
	@ExceptionHandler(MsgApiException.class)
	public ResponseEntity<ResponseApi> erroGeralMsgApiException(MsgApiException ex, 
			                                                   HttpServletRequest request) {
		
		ex.printStackTrace();
		
		ResponseApi responseApi = new ResponseApi(LocalDateTime.now(),
				                ex.getStatus().value(),
				                ex.getStatus().getReasonPhrase(),
				                ex.getMessage(), 
				                request.getRequestURI());

		return ResponseEntity.status(ex.getStatus())
				             .contentType(MediaType.APPLICATION_JSON)
				             .body(responseApi);
	}

}
