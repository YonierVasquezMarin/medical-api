package com.example.medical_api.domain.model;

import com.example.medical_api.domain.enums.UserRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
	@SequenceGenerator(name = "users_seq", sequenceName = "USERS_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "FIRST_NAME", nullable = false, length = 100)
	private String firstName;

	@Column(name = "LAST_NAME", nullable = false, length = 100)
	private String lastName;

	@Column(name = "PHONE", length = 30)
	private String phone;

	@Column(name = "EMAIL", nullable = false, unique = true, length = 255)
	private String email;

	@Column(name = "PASSWORD_HASHED", nullable = false, length = 255)
	private String passwordHashed;

	@Enumerated(EnumType.STRING)
	@Column(name = "ROL", nullable = false, length = 50)
	private UserRole role;
}
