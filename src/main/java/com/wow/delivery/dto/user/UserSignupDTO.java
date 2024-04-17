package com.wow.delivery.dto.user;

import com.wow.delivery.entity.User;
import com.wow.delivery.util.PasswordEncoder;
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
        PasswordEncodingDTO passwordEncoder = PasswordEncoder.encodePassword(this.password);
        return User.builder().email(this.email)
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber(this.phoneNumber)
                .build();
    }

}
