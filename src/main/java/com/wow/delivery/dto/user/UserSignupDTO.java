package com.wow.delivery.dto.user;

import com.wow.delivery.dto.common.PasswordEncodingDTO;
import com.wow.delivery.entity.User;
import com.wow.delivery.util.PasswordEncoder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignupDTO {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")
    private String email;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    @NotNull
    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$")
    private String phoneNumber;

    public UserSignupDTO() {
    }

    @Builder
    public UserSignupDTO(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User toEntity() {
        PasswordEncodingDTO passwordEncoder = PasswordEncoder.encodePassword(this.password);
        return User.builder().email(this.email)
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber(this.phoneNumber)
                .build();
    }

}
