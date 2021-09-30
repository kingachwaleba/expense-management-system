package com.team.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserStatusNotFoundException extends RuntimeException {

    public UserStatusNotFoundException() {
        super("Could not find user status!");
    }
}