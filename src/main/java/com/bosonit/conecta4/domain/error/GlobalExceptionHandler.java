package com.bosonit.conecta4.domain.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomError> handleEntityNotFoundException(EntityNotFoundException ex) {
        CustomError error = ex.getError();
        return ResponseEntity.status(error.getHttpCode()).body(error);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<CustomError> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        CustomError error = ex.getError();
        return ResponseEntity.status(error.getHttpCode()).body(error);
    }
}