package com.wow.delivery.dto.rider;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RiderSigninDTO {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")
    private String email;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    public RiderSigninDTO() {}

    @Builder
    public RiderSigninDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
