package com.team.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ImageLoadException extends RuntimeException {

    public ImageLoadException() {
        super("Could not load the image!");
    }
}
