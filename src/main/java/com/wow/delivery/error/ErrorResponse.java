package com.wow.delivery.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Getter
public class ErrorResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<BindingError> bindingErrors;

    private ErrorResponse(RuntimeException e) {
        this.code = e.getClass().getSimpleName();
        this.message = e.getMessage();
    }

    private ErrorResponse(MethodArgumentNotValidException e, ErrorCode errorCode) {
        this.code = e.getClass().getSimpleName();
        this.message = errorCode.getMessage();
        this.bindingErrors = BindingError.of(e.getBindingResult());
    }

    public static ErrorResponse of(RuntimeException e) {
        return new ErrorResponse(e);
    }

    public static ErrorResponse of(MethodArgumentNotValidException e, ErrorCode errorCode) {
        return new ErrorResponse(e, errorCode);
    }
}
