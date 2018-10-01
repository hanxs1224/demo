package com.example.exception;

public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException(String message) {
        super(message);
    }

}
