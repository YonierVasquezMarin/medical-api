package com.example.medical_api.application.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.medical_api.application.dto.CreateUserRequest;
import com.example.medical_api.application.dto.UserResponse;
import com.example.medical_api.application.exception.EmailAlreadyExistsException;
import com.example.medical_api.application.exception.UserNotFoundException;
import com.example.medical_api.domain.model.User;
import com.example.medical_api.domain.repository.UserRepository;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	private final UserRepository _userRepository;
	private final PasswordEncoder _passwordEncoder;

	private CreateUserRequest _createUserRequest;
	private User _user;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		_userRepository = userRepository;
		_passwordEncoder = passwordEncoder;
	}

	@Transactional
	public UserResponse createUser(CreateUserRequest request) {
		try {
			_createUserRequest = request;
			return EjecutarCreacionDeUsuario();
		} catch (EmailAlreadyExistsException ex) {
			throw ex;
		} catch (Exception ex) {
			LOGGER.error("Error al crear usuario", ex);
			throw ex;
		}
	}

	@Transactional(readOnly = true)
	public List<UserResponse> listUsers() {
		try {
			return EjecutarListadoDeUsuarios();
		} catch (Exception ex) {
			LOGGER.error("Error al listar usuarios", ex);
			throw ex;
		}
	}

	@Transactional
	public void deleteUser(Long id) {
		try {
			EjecutarEliminacionDeUsuario(id);
		} catch (UserNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			LOGGER.error("Error al eliminar usuario", ex);
			throw ex;
		}
	}

	private UserResponse EjecutarCreacionDeUsuario() {
		ValidarEmailDisponible();
		_user = ConstruirUsuario();
		_user = _userRepository.save(_user);
		RegistrarLog_UsuarioCreado();
		return MapearUsuarioARespuesta(_user);
	}

	private List<UserResponse> EjecutarListadoDeUsuarios() {
		return _userRepository.findAll().stream()
				.map(this::MapearUsuarioARespuesta)
				.toList();
	}

	private void EjecutarEliminacionDeUsuario(Long id) {
		if (UsuarioNoExiste(id)) {
			throw new UserNotFoundException(id);
		}
		_userRepository.deleteById(id);
		RegistrarLog_UsuarioEliminado(id);
	}

	private void ValidarEmailDisponible() {
		if (_userRepository.existsByEmail(_createUserRequest.getEmail())) {
			throw new EmailAlreadyExistsException(_createUserRequest.getEmail());
		}
	}

	private boolean UsuarioNoExiste(Long id) {
		boolean usuarioExiste = _userRepository.existsById(id);
		return !usuarioExiste;
	}

	private User ConstruirUsuario() {
		User user = new User();
		user.setPasswordHashed(ObtenerPasswordHasheado());
		user.setFirstName(_createUserRequest.getFirstName());
		user.setLastName(_createUserRequest.getLastName());
		user.setEmail(_createUserRequest.getEmail());
		user.setPhone(_createUserRequest.getPhone());
		user.setRole(_createUserRequest.getRole());
		return user;
	}

	private String ObtenerPasswordHasheado() {
		return _passwordEncoder.encode(_createUserRequest.getPassword());
	}

	private UserResponse MapearUsuarioARespuesta(User user) {
		return UserResponse.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.phone(user.getPhone())
				.email(user.getEmail())
				.role(user.getRole())
				.id(user.getId())
				.build();
	}

	private void RegistrarLog_UsuarioCreado() {
		LOGGER.info("Usuario creado con email {}", _user.getEmail());
	}

	private void RegistrarLog_UsuarioEliminado(Long id) {
		LOGGER.info("Usuario eliminado con id {}", id);
	}
}
