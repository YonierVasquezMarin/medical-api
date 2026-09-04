package com.example.medical_api.application.exception;

public class InvalidCredentialsException extends RuntimeException {

	public InvalidCredentialsException() {
		super("Credenciales inválidas");
	}
}
