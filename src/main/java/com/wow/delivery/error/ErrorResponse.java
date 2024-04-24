package com.wow.delivery.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wow.delivery.error.exception.CustomException;
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

    private ErrorResponse(CustomException e) {
        this.code = e.getClass().getSimpleName();
        this.message = e.getErrorCode().getMessage();
    }

    private ErrorResponse(MethodArgumentNotValidException e, ErrorCode errorCode) {
        this.code = e.getClass().getSimpleName();
        this.message = errorCode.getMessage();
        this.bindingErrors = BindingError.of(e.getBindingResult());
    }

    public static ErrorResponse of(CustomException e) {
        return new ErrorResponse(e);
    }

    public static ErrorResponse of(MethodArgumentNotValidException e, ErrorCode errorCode) {
        return new ErrorResponse(e, errorCode);
    }
}
