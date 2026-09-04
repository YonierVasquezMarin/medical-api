package com.example.medical_api.config;

import org.springframework.boot.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalTomcatJndiRebindConfig {

	@Bean
	TomcatContextCustomizer reenlazarDataSourceLocalEnJndi() {
		return context -> LocalJndiDataSourceInitializer.ReenlazarDataSourceEnJndi();
	}
}
