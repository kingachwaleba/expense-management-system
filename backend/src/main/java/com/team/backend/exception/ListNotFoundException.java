package com.team.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ListNotFoundException extends RuntimeException {

    public ListNotFoundException() {
        super("Could not find list!");
    }
}
