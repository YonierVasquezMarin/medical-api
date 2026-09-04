package com.example.medical_api.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.medical_api.application.dto.LoginRequest;
import com.example.medical_api.application.dto.LoginResponse;
import com.example.medical_api.application.exception.InvalidCredentialsException;
import com.example.medical_api.domain.model.User;
import com.example.medical_api.domain.repository.UserRepository;
import com.example.medical_api.infrastructure.security.JwtService;

@Service
public class AuthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	private final UserRepository _userRepository;
	private final PasswordEncoder _passwordEncoder;
	private final JwtService _jwtService;

	private LoginRequest _loginRequest;
	private User _user;

	public AuthService(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService) {
		_userRepository = userRepository;
		_passwordEncoder = passwordEncoder;
		_jwtService = jwtService;
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {
		try {
			_loginRequest = request;
			return EjecutarLogin();
		} catch (InvalidCredentialsException ex) {
			throw ex;
		} catch (Exception ex) {
			LOGGER.error("Error al autenticar usuario", ex);
			throw ex;
		}
	}

	private LoginResponse EjecutarLogin() {
		_user = ObtenerUsuarioPorEmail();
		ValidarPassword();
		RegistrarLog_LoginExitoso();
		return ConstruirRespuestaLogin();
	}

	private User ObtenerUsuarioPorEmail() {
		return _userRepository.findByEmail(_loginRequest.getEmail())
				.orElseThrow(InvalidCredentialsException::new);
	}

	private void ValidarPassword() {
		if (PasswordNoCoincide()) {
			throw new InvalidCredentialsException();
		}
	}

	private boolean PasswordNoCoincide() {
		boolean passwordCoincide = _passwordEncoder.matches(
				_loginRequest.getPassword(),
				_user.getPasswordHashed());
		return !passwordCoincide;
	}

	private LoginResponse ConstruirRespuestaLogin() {
		return LoginResponse.builder()
				.token(_jwtService.generateToken(_user))
				.expiresInMs(_jwtService.getExpirationMs())
				.tokenType("Bearer")
				.build();
	}

	private void RegistrarLog_LoginExitoso() {
		LOGGER.info("Login exitoso para {}", _user.getEmail());
	}
}
