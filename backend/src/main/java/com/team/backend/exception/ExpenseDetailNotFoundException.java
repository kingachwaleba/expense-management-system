package com.team.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ExpenseDetailNotFoundException extends RuntimeException {

    public ExpenseDetailNotFoundException() {
        super("Could not find expense detail!");
    }
}