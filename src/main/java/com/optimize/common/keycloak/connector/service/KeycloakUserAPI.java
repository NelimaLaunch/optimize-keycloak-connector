package com.optimize.common.keycloak.connector.service;

import com.optimize.common.keycloak.connector.config.KeycloakProperties;
import com.optimize.common.keycloak.connector.exception.UserAlreadyExistsException;
import com.optimize.common.keycloak.connector.model.keycloak.KeycloakUser;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserAPI {
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public Response createUser(KeycloakUser user) {
        UserRepresentation userRepresentation = getUserRepresentation(user);
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(Boolean.FALSE);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        userRepresentation.setCredentials(List.of(credential));
        //log.debug("COUNT ===> {}", keycloak.realm(keycloakProperties.getRealm()).users().count());
        Response response;
        try {
            response = keycloak.realm(keycloakProperties.getRealm()).users().create(userRepresentation);
        } catch (Exception e) {
            log.error("CREATE ERROR: {}", e.getMessage());
            throw new RuntimeException("Failed to create user: " + e.getLocalizedMessage());
        } catch (Error error) {
            log.error("ERROR: {}", error.getMessage());
            throw new RuntimeException("Failed to create user: " + error.getLocalizedMessage());
        }
        if (response.getStatus() != 201) {
            if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                throw new UserAlreadyExistsException();
            }
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo().getReasonPhrase());
        }
        return response;
    }

    public UserRepresentation getFullUserRepresentation(KeycloakUser user) {
        UserRepresentation userRepresentation = getUserRepresentation(user);
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(Boolean.FALSE);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        userRepresentation.setCredentials(List.of(credential));
        return userRepresentation;
    }

    private static UserRepresentation getUserRepresentation(KeycloakUser user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setRealmRoles(user.getRoles());
        userRepresentation.setGroups(user.getGroups());
        userRepresentation.setEnabled(Boolean.TRUE);
        userRepresentation.setEmailVerified(Boolean.TRUE);
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put("gender", List.of(user.getGender()));
        userRepresentation.setAttributes(attributes);
        return userRepresentation;
    }
}
