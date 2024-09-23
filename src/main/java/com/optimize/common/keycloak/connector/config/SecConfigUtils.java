package com.optimize.common.keycloak.connector.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SecConfigUtils {
    private final String authorizeApiUrl;
    private final String keycloakIssuer;

    public SecConfigUtils(@Value(value = "${keycloak.authorize-api.list}") String authorizeApiUrl, @Value(value = "${keycloak.realm-url}") String keycloakIssuer) {
        this.authorizeApiUrl = authorizeApiUrl;
        this.keycloakIssuer = keycloakIssuer;
    }

    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }



    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                getUnsecureAPI()).permitAll()
                        .anyRequest().authenticated()
                ).oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    protected String[] getUnsecureAPI() {
        List<String> staticAPI = List.of("/i18n/**",
                "/content/**",
                "/v3/api-docs/swagger-config/**",
                "/v3/api-docs/**",
                "/v2/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui/index.html#/",
                "/apidoc/**",
                "/swagger-resources/**",
                "/actuator/**",
                "/swagger-ui.html",
                "/test/**",
                "/api/v1/iam/users") ;
        String[] apis = authorizeApiUrl.split(",");
        String[] staticAPITableau = staticAPI.toArray(new String[0]);
        // Concaténer les deux tableaux
        String[] result = new String[staticAPITableau.length + apis.length];
        System.arraycopy(staticAPITableau, 0, result, 0, staticAPITableau.length);
        System.arraycopy(apis, 0, result, staticAPITableau.length, apis.length);
        log.debug("UNSECURE API : {}", (Object) result);
        return result;
    }

    // Méthode pour mapper les rôles du token JWT dans les autorités Spring Security
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            //Map<String, Object> realm_access = (Map<String, Object>) jwt.getClaims().get("realm_access");
            List<String> roles  = new ArrayList<>();
            List<String> claimsRoles = (List<String>) jwt.getClaims().get("roles");
            if (Objects.nonNull(claimsRoles)) {
                roles.addAll(claimsRoles);
            }
            return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
                    Collectors.toList());
        });
        return converter;
    }

    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(keycloakIssuer);
    }





    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
                Collectors.toList());
    }
}
