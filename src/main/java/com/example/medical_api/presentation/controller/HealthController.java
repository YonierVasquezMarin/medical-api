package com.example.medical_api.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.medical_api.application.dto.HealthResponse;
import com.example.medical_api.application.service.HealthService;

@RestController
@RequestMapping("/api/health")
public class HealthController {

	private final HealthService _healthService;

	public HealthController(HealthService healthService) {
		_healthService = healthService;
	}

	@GetMapping
	public ResponseEntity<HealthResponse> getHealth() {
		HealthResponse health = _healthService.getHealth();
		if (ServicioNoEstaDisponible(health)) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
		}
		return ResponseEntity.ok(health);
	}

	private boolean ServicioNoEstaDisponible(HealthResponse health) {
		boolean servicioDisponible = "UP".equals(health.getStatus());
		return !servicioDisponible;
	}
}
