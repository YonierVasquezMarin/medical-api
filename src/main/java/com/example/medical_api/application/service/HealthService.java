package com.example.medical_api.application.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.medical_api.application.dto.HealthResponse;

@Service
public class HealthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthService.class);
	private static final String STATUS_UP = "UP";
	private static final String STATUS_DOWN = "DOWN";
	private static final int CONNECTION_TIMEOUT_SECONDS = 2;

	private final DataSource _dataSource;

	private boolean _databaseIsUp;

	public HealthService(DataSource dataSource) {
		_dataSource = dataSource;
	}

	public HealthResponse getHealth() {
		try {
			return EjecutarConsultaDeSalud();
		} catch (Exception ex) {
			LOGGER.error("Error al consultar el estado de salud", ex);
			throw ex;
		}
	}

	private HealthResponse EjecutarConsultaDeSalud() {
		_databaseIsUp = VerificarConexionConBaseDeDatos();
		RegistrarLog_EstadoDeSalud();
		return ConstruirRespuestaDeSalud();
	}

	private boolean VerificarConexionConBaseDeDatos() {
		try (Connection connection = _dataSource.getConnection()) {
			return ConexionEsValida(connection);
		} catch (SQLException ex) {
			RegistrarLog_ConexionConBaseDeDatosFallida(ex);
			return false;
		}
	}

	private boolean ConexionEsValida(Connection connection) throws SQLException {
		return connection.isValid(CONNECTION_TIMEOUT_SECONDS);
	}

	private HealthResponse ConstruirRespuestaDeSalud() {
		String databaseStatus = ObtenerEstadoDeBaseDeDatos();
		String overallStatus = ObtenerEstadoGeneral();
		return HealthResponse.builder()
				.database(databaseStatus)
				.status(overallStatus)
				.api(STATUS_UP)
				.build();
	}

	private String ObtenerEstadoDeBaseDeDatos() {
		if (_databaseIsUp) {
			return STATUS_UP;
		}
		return STATUS_DOWN;
	}

	private String ObtenerEstadoGeneral() {
		if (_databaseIsUp) {
			return STATUS_UP;
		}
		return STATUS_DOWN;
	}

	private void RegistrarLog_EstadoDeSalud() {
		LOGGER.info("Estado de salud consultado. api={}, database={}", STATUS_UP, ObtenerEstadoDeBaseDeDatos());
	}

	private void RegistrarLog_ConexionConBaseDeDatosFallida(SQLException ex) {
		LOGGER.warn("No fue posible validar la conexión con la base de datos: {}", ex.getMessage());
	}
}
