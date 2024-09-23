package com.optimize.common.keycloak.connector.exception;

public class MailAlreadyExistsException extends RuntimeException {

    public MailAlreadyExistsException() {
        super("E-Mail already used.");
    }
}
