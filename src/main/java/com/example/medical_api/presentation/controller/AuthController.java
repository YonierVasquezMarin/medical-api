package com.example.medical_api.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.medical_api.application.dto.LoginRequest;
import com.example.medical_api.application.dto.LoginResponse;
import com.example.medical_api.application.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService _authService;

	public AuthController(AuthService authService) {
		_authService = authService;
	}

	@PostMapping("/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request) {
		return _authService.login(request);
	}
}
