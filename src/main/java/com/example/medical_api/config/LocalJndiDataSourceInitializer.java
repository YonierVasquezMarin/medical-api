package com.example.medical_api.config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class LocalJndiDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	public static final String NOMBRE_JNDI_LOCAL = "java:comp/env/jdbc/MedicalDS";

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalJndiDataSourceInitializer.class);

	private ConfigurableEnvironment _environment;
	private HikariDataSource _dataSource;

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		try {
			_environment = applicationContext.getEnvironment();
			PublicarDataSourceEnJndiCuandoElPerfilEsLocal();
		} catch (Exception ex) {
			throw new IllegalStateException("No fue posible publicar el DataSource local en JNDI", ex);
		}
	}

	private void PublicarDataSourceEnJndiCuandoElPerfilEsLocal() throws NamingException {
		if (PerfilLocalNoEstaActivo()) {
			return;
		}
		ValidarCredencialesLocales();
		_dataSource = ConstruirDataSourceLocal();
		ActivarContextoJndiEnMemoria();
		EnlazarDataSourceEnJndi();
		RegistrarLog_DataSourceLocalPublicadoEnJndi();
	}

	private boolean PerfilLocalNoEstaActivo() {
		boolean perfilLocalActivo = _environment.acceptsProfiles(Profiles.of("local"));
		return !perfilLocalActivo;
	}

	private void ValidarCredencialesLocales() {
		if (UsuarioLocalEstaVacio() || ContrasenaLocalEstaVacia()) {
			throw new IllegalStateException(
					"Defina local.datasource.username y local.datasource.password "
							+ "(o DATABASE_USERNAME / DATABASE_PASSWORD) fuera del repositorio.");
		}
	}

	private boolean UsuarioLocalEstaVacio() {
		return CadenaEstaVacia(ObtenerUsuarioLocal());
	}

	private boolean ContrasenaLocalEstaVacia() {
		return CadenaEstaVacia(ObtenerContrasenaLocal());
	}

	private boolean CadenaEstaVacia(String valor) {
		return valor == null || valor.isBlank();
	}

	private HikariDataSource ConstruirDataSourceLocal() {
		HikariConfig configuracion = new HikariConfig();
		configuracion.setJdbcUrl(ObtenerUrlLocal());
		configuracion.setUsername(ObtenerUsuarioLocal());
		configuracion.setPassword(ObtenerContrasenaLocal());
		configuracion.setDriverClassName(ObtenerDriverLocal());
		configuracion.setPoolName("MedicalLocalPool");
		return new HikariDataSource(configuracion);
	}

	private String ObtenerUrlLocal() {
		return _environment.getProperty("local.datasource.url");
	}

	private String ObtenerUsuarioLocal() {
		return _environment.getProperty("local.datasource.username");
	}

	private String ObtenerContrasenaLocal() {
		return _environment.getProperty("local.datasource.password");
	}

	private String ObtenerDriverLocal() {
		return _environment.getProperty("local.datasource.driver-class-name", "oracle.jdbc.OracleDriver");
	}

	private void ActivarContextoJndiEnMemoria() {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.MemoryContextFactory");
		System.setProperty("org.osjava.sj.jndi.shared", "true");
		System.setProperty("org.osjava.sj.delimiter", "/");
	}

	private void EnlazarDataSourceEnJndi() throws NamingException {
		Context contexto = new InitialContext();
		contexto.rebind(NOMBRE_JNDI_LOCAL, _dataSource);
	}

	private void RegistrarLog_DataSourceLocalPublicadoEnJndi() {
		LOGGER.info("DataSource local publicado en JNDI como {}", NOMBRE_JNDI_LOCAL);
	}
}
