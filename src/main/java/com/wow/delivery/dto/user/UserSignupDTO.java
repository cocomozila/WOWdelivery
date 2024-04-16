package com.wow.delivery.dto.user;

import com.wow.delivery.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserSignupDTO {

    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String phoneNumber;

    public User toEntity() {
        return User.builder().email(this.email)
                .password(this.password)
                .phoneNumber(this.phoneNumber)
                .build();
    }

}
