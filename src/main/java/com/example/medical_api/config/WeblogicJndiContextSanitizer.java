package com.example.medical_api.config;

import javax.naming.Context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class WeblogicJndiContextSanitizer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final String FABRICA_JNDI_LOCAL = "org.osjava.sj.MemoryContextFactory";

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		LimpiarFabricaJndiLocalSiQuedoActiva();
	}

	private void LimpiarFabricaJndiLocalSiQuedoActiva() {
		if (FabricaJndiLocalNoEstaActiva()) {
			return;
		}
		System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
		System.clearProperty("org.osjava.sj.jndi.shared");
		System.clearProperty("org.osjava.sj.jndi.ignoreClose");
		System.clearProperty("org.osjava.sj.delimiter");
	}

	private boolean FabricaJndiLocalNoEstaActiva() {
		return !FABRICA_JNDI_LOCAL.equals(System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
	}
}
