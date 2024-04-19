package com.wow.delivery.error;

import com.wow.delivery.error.exception.DuplicateException;
import com.wow.delivery.error.exception.MismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), e.getErrorCode().getStatus());
    }

    @ExceptionHandler(MismatchException.class)
    public ResponseEntity<ErrorResponse> handleMismatchException(MismatchException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), e.getErrorCode().getStatus());
    }
}
