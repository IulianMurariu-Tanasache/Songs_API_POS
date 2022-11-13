package com.pos.idm.exception;

public class InvalidJWTException extends Exception {
    public InvalidJWTException() {
        super("Token cannot be trusted!");
    }
}
