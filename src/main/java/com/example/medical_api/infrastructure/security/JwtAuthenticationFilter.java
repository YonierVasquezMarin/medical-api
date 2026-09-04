package com.example.medical_api.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtService _jwtService;

	private String _authorizationHeader;
	private Claims _claims;

	public JwtAuthenticationFilter(JwtService jwtService) {
		_jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		try {
			EstablecerAutenticacionDesdeToken(request);
		} catch (JwtException | IllegalArgumentException ex) {
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);
	}

	private void EstablecerAutenticacionDesdeToken(HttpServletRequest request) {
		_authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (EncabezadoBearerNoEstaPresente()) {
			return;
		}
		_claims = _jwtService.parseClaims(ExtraerToken());
		AsignarContextoDeSeguridad();
	}

	private boolean EncabezadoBearerNoEstaPresente() {
		boolean encabezadoPresente = _authorizationHeader != null && _authorizationHeader.startsWith(BEARER_PREFIX);
		return !encabezadoPresente;
	}

	private String ExtraerToken() {
		return _authorizationHeader.substring(BEARER_PREFIX.length());
	}

	private void AsignarContextoDeSeguridad() {
		String role = _claims.get("role", String.class);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				_claims.getSubject(),
				null,
				List.of(new SimpleGrantedAuthority("ROLE_" + role)));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
