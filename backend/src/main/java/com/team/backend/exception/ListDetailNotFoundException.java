package com.team.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ListDetailNotFoundException extends RuntimeException {

    public ListDetailNotFoundException() {
        super("Could not find list detail!");
    }
}
