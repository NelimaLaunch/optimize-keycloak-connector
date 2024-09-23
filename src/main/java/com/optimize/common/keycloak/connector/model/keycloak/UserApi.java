package com.optimize.common.keycloak.connector.model.keycloak;

import com.optimize.common.keycloak.connector.exception.MailAlreadyExistsException;

public interface UserApi {
    KeycloakUser getUser(String userId);

    KeycloakUser findUserByEmail(String email);

    KeycloakUser createUser(KeycloakUser user) throws MailAlreadyExistsException;

    KeycloakUser createUser(KeycloakUser user, String password) throws MailAlreadyExistsException;

    void updateUser(KeycloakUser user);

    void updatePassword(String password, String keycloakUserId);

    void forgotPassword(String keycloakId);

    void disableUser(String keycloakUserId);

    void enableUser(String keycloakUserId);
}
