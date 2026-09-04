package com.example.medical_api.application.dto;

import com.example.medical_api.domain.enums.UserRole;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private UserRole role;
}
