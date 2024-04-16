package com.wow.delivery.error;

import com.wow.delivery.error.exception.DuplicateEmailException;
import com.wow.delivery.error.exception.DuplicatePhoneNumberException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@RestControllerAdvice
public class ExceptionController {

    private String getSimpleName(Exception e) {
        return e.getClass().getSimpleName();
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ErrorMessage handleDuplicateEmailException(DuplicateEmailException e) {
        return new ErrorMessage(e.getLocalizedMessage(), getSimpleName(e));
    }

    @ExceptionHandler(DuplicatePhoneNumberException.class)
    public ErrorMessage handleDuplicatePhoneNumberException(DuplicatePhoneNumberException e) {
        return new ErrorMessage(e.getLocalizedMessage(), getSimpleName(e));
    }
}
