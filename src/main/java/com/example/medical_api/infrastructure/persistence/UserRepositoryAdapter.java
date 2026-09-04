package com.example.medical_api.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.medical_api.domain.model.User;
import com.example.medical_api.domain.repository.UserRepository;

@Repository
public class UserRepositoryAdapter implements UserRepository {

	private final UserJpaRepository _userJpaRepository;

	public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
		_userJpaRepository = userJpaRepository;
	}

	@Override
	public User save(User user) {
		return _userJpaRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return _userJpaRepository.findAll();
	}

	@Override
	public Optional<User> findById(Long id) {
		return _userJpaRepository.findById(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return _userJpaRepository.findByEmail(email);
	}

	@Override
	public boolean existsByEmail(String email) {
		return _userJpaRepository.existsByEmail(email);
	}

	@Override
	public boolean existsById(Long id) {
		return _userJpaRepository.existsById(id);
	}

	@Override
	public void deleteById(Long id) {
		_userJpaRepository.deleteById(id);
	}
}
