package com.example.medical_api.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.medical_api.domain.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey _signingKey;
	private final long _expirationMs;

	public JwtService(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration-ms}") long expirationMs) {
		_signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		_expirationMs = expirationMs;
	}

	public String generateToken(User user) {
		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() + _expirationMs);
		return Jwts.builder()
				.subject(user.getEmail())
				.claim("role", user.getRole().name())
				.issuedAt(issuedAt)
				.expiration(expiration)
				.signWith(_signingKey)
				.compact();
	}

	public Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(_signingKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public long getExpirationMs() {
		return _expirationMs;
	}
}
