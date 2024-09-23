//package com.optimize.common.keycloak.connector.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtDecoders;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.session.HttpSessionEventPublisher;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Configuration
//class SecurityConfig {
//
//    private static final String GROUPS = "groups";
//    private static final String REALM_ACCESS_CLAIM = "realm_access";
//    private static final String ROLES_CLAIM = "roles";
//
//    @Value("${keycloak.authorize-api.list}")
//    protected String authorizeApiUrl;
//    @Value("${keycloak.base-url}")
//    protected String keycloakIssuer;
//
//
//
//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }
//
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers(
//                                getUnsecureAPI()).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2ResourceServer((oauth2) -> oauth2
//                        .jwt(Customizer.withDefaults())
//                ).sessionManagement(httpSecuritySessionManagementConfigurer ->
//                        httpSecuritySessionManagementConfigurer
//                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        return http.build();
//    }
//
//    protected String[] getUnsecureAPI() {
//        List<String> staticAPI = List.of("/i18n/**",
//                "/content/**",
//                "/v3/api-docs/swagger-config/**",
//                "/v3/api-docs/**",
//                "/v2/api-docs/**",
//                "/swagger-ui/**",
//                "/swagger-ui/index.html#/",
//                "/apidoc/**",
//                "/swagger-resources/**",
//                "/actuator/**",
//                "/swagger-ui.html",
//                "/test/**") ;
//        String[] apis = authorizeApiUrl.split(",");
//        String[] staticAPITableau = staticAPI.toArray(new String[0]);
//        // Concaténer les deux tableaux
//        String[] result = new String[staticAPITableau.length + apis.length];
//        System.arraycopy(staticAPITableau, 0, result, 0, staticAPITableau.length);
//        System.arraycopy(apis, 0, result, staticAPITableau.length, apis.length);
//        return result;
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return JwtDecoders.fromIssuerLocation(keycloakIssuer);
//    }
//
//
//
//
//
//    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
//        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
//                Collectors.toList());
//    }
//
//
//    @Profile("local")
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Collections.singletonList("*"));
//        configuration.setAllowedMethods(Collections.singletonList("*"));
//        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        //configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//
//
//}
