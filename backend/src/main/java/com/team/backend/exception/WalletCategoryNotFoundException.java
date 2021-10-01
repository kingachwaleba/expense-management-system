package com.team.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WalletCategoryNotFoundException extends RuntimeException {

    public WalletCategoryNotFoundException() {
        super("Could not find wallet category!");
    }
}
