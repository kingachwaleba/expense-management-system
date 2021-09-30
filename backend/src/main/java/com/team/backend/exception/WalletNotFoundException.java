package com.team.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException() {
        super("Could not find wallet!");
    }
}
