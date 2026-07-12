package com.springone.exception;

import java.time.LocalDateTime;

public class ResponseApi {

	private LocalDateTime localDateTime;
	private int status;
	private String error;
	private String message;
	private String path;

	public ResponseApi(LocalDateTime localDateTime, int status, String error, String message, String path) {
		this.localDateTime = localDateTime;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

}
