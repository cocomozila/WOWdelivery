package com.wow.delivery.dto.owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OwnerSigninDTO {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")
    private String email;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    public OwnerSigninDTO() {}

    @Builder
    public OwnerSigninDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
