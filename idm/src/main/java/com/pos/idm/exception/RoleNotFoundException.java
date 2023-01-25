package com.pos.idm.exception;

public class RoleNotFoundException extends Exception{
    public RoleNotFoundException(String message) {
        super("Role not found!");
    }
}
