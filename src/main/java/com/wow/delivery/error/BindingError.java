package com.wow.delivery.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BindingError {

    private String field;
    private String value;
    private String reason;

    @Builder
    private BindingError(String field, String value, String reason) {
        this.field = field;
        this.value = value;
        this.reason = reason;
    }

    public static List<BindingError> of(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
            .map(error -> new BindingError(
                error.getField(),
                error.getRejectedValue() == null ? null : error.getRejectedValue().toString(),
                error.getDefaultMessage() == null ? null : toDefaultMessage(error.getField())))
            .collect(Collectors.toList());
    }

    private static String toDefaultMessage(String fieldName) {
        return fieldName + "가 올바른 형식이 아닙니다.";
    }
}
