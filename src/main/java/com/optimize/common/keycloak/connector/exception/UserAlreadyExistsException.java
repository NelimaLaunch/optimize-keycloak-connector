package com.optimize.common.keycloak.connector.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("user.already.exists");
    }
}
