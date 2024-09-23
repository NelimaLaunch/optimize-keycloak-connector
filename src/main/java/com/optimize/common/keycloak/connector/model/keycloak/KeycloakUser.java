package com.optimize.common.keycloak.connector.model.keycloak;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * A user in keycloak. This is the model class to be used when managing users in keycloak with this wrapper class.
 * It contains more fields than the normal UserRepresentation class from keycloak, like roles and groups.
 */
@Getter
@Builder
public final class KeycloakUser {

    private final String id;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String gender;
    private final String locale;
    private final List<String> groups;
    private final List<String> roles;

}
