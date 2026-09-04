package com.example.medical_api.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HealthResponse {

	private String status;
	private String api;
	private String database;
}
