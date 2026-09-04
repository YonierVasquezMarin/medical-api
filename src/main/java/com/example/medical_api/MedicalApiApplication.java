package com.example.medical_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.example.medical_api.config.LocalJndiDataSourceInitializer;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class MedicalApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(MedicalApiApplication.class);
		application.addInitializers(new LocalJndiDataSourceInitializer());
		application.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MedicalApiApplication.class)
				.initializers(new LocalJndiDataSourceInitializer());
	}
}
