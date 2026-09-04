package com.example.medical_api.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

	private String token;
	private String tokenType;
	private long expiresInMs;
}
