package com.team.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WalletUserNotFoundException extends RuntimeException {

    public WalletUserNotFoundException() {
        super("Could not find wallet user!");
    }
}