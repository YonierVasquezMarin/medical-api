package com.example.medical_api.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.medical_api.domain.model.User;

public interface UserRepository {

	User save(User user);

	List<User> findAll();

	Optional<User> findById(Long id);

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsById(Long id);

	void deleteById(Long id);
}
