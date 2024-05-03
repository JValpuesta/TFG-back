package com.valpuestajorge.conecta4.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomError> handleEntityNotFoundException(NotFoundException ex) {
        CustomError error = ex.getError();
        return ResponseEntity.status(error.getHttpCode()).body(error);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<CustomError> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        CustomError error = ex.getError();
        return ResponseEntity.status(error.getHttpCode()).body(error);
    }
}