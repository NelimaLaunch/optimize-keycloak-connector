package com.optimize.common.keycloak.connector.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakProperties {
    private String serverUrl;
    private String baseUrl;
    private String realm;
    private String clientId;
    private String realmUrl;
    private String username;
    private String password;
    private String scope;
    private String grantType;
    private Credentials credentials;

    @Data
    static class Credentials {
        private String secret;
    }

}
