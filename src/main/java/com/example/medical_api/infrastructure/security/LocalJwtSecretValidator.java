package com.example.medical_api.infrastructure.security;

import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("local")
public class LocalJwtSecretValidator {

	private final Environment _environment;

	public LocalJwtSecretValidator(Environment environment) {
		_environment = environment;
	}

	@PostConstruct
	public void validateLocalJwtSecret() {
		try {
			ValidarSecretoJwtLocal();
		} catch (IllegalStateException ex) {
			throw ex;
		}
	}

	private void ValidarSecretoJwtLocal() {
		if (SecretoJwtEstaVacio() || SecretoJwtEsDemasiadoCorto()) {
			throw new IllegalStateException(
					"Defina jwt.secret en application-local.properties (mínimo 32 caracteres) "
							+ "o la variable JWT_SECRET.");
		}
	}

	private boolean SecretoJwtEstaVacio() {
		String secret = ObtenerSecretoJwt();
		return secret == null || secret.isBlank();
	}

	private boolean SecretoJwtEsDemasiadoCorto() {
		return ObtenerSecretoJwt().length() < 32;
	}

	private String ObtenerSecretoJwt() {
		return _environment.getProperty("jwt.secret");
	}
}
