package com.wow.delivery.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordEncodingDTO {

    private String salt;
    private String encodePassword;
}
