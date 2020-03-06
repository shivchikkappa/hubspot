package com.hubspot.exception;

public class PartnersNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PartnersNotFoundException(String message) {
        super(message);
    }
}
