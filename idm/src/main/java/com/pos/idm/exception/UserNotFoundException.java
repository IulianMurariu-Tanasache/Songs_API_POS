package com.pos.idm.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User entity not found");
    }
}
