package com.optimize.common.keycloak.connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan({"com.optimize.common.keycloak.connector.config"})
public class KeycloakConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakConnectorApplication.class, args);
	}

}
