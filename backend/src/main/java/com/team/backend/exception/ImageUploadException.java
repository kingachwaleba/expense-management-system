package com.team.backend.exception;

import com.team.backend.config.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ImageUploadException extends ResponseEntityExceptionHandler {

    private final ErrorMessage errorMessage;

    public ImageUploadException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exception) {
        return new ResponseEntity<>(errorMessage.get("image.tooBig"), HttpStatus.EXPECTATION_FAILED);
    }
}
