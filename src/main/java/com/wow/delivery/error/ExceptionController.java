package com.wow.delivery.error;

import com.wow.delivery.error.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ErrorResponse.of(e, ErrorCode.INVALID_PARAMETER), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        if (DataNotFoundException.isInstanceOf(e)) {
            return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.BAD_REQUEST);
    }
}
