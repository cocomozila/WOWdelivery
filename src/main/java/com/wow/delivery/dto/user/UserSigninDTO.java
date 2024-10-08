package com.wow.delivery.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSigninDTO {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")
    private String email; // 이메일

    @NotBlank
    @Size(min = 8, max = 16)
    private String password; // 패스워드

    public UserSigninDTO() {}

    @Builder
    public UserSigninDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
