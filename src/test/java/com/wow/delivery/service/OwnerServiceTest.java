package com.wow.delivery.service;

import com.wow.delivery.dto.owner.OwnerSignupDTO;
import com.wow.delivery.entity.Owner;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.OwnerRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OwnerServiceTest {

    @Autowired
    OwnerService ownerService;

    @Autowired
    OwnerRepository ownerRepository;

    @AfterEach
    void after() {
        ownerRepository.deleteAll();
    }

    @Test
    @DisplayName("가게사장 회원가입 성공")
    void ownerSignupSuccessTest() {
        Owner signupOwner = OwnerSignupDTO.builder()
            .email("owner1@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build().toEntity();

        ownerService.signup(signupOwner);
        Owner findOwner = ownerRepository.getOwnerByEmail(signupOwner.getEmail());
        assertThat(findOwner.getId()).isNotNull();
    }

    @Test
    @DisplayName("가게사장 회원가입 실패 - 중복된 이메일")
    void signupFailTest_DuplicateEmail() {
        Owner signupOwner = OwnerSignupDTO.builder()
            .email("owner@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build().toEntity();

        ownerService.signup(signupOwner);

        Owner duplicateEmailSignupOwner = OwnerSignupDTO.builder()
            .email("owner@gmail.com")
            .password("12345678")
            .phoneNumber("01011113333")
            .build().toEntity();

        Assertions.assertThrows(InvalidParameterException.class,
            () -> ownerService.signup(duplicateEmailSignupOwner));
    }

    @Test
    @DisplayName("가게사장 회원가입 실패 - 중복된 휴대폰 번호")
    void signupFailTest_DuplicatePhoneNumber() {
        Owner signupOwner = OwnerSignupDTO.builder()
            .email("owner@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build().toEntity();

        ownerService.signup(signupOwner);

        Owner duplicatePhoneNumberSignupOwner = OwnerSignupDTO.builder()
            .email("another@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build().toEntity();

        Assertions.assertThrows(InvalidParameterException.class,
            () -> ownerService.signup(duplicatePhoneNumberSignupOwner));
    }

    @Test
    @DisplayName("가게사장 로그인 성공 - 세션에 이메일 등록")
    void signinSuccessTest() {
        OwnerSignupDTO signupOwner = OwnerSignupDTO.builder()
            .email("owner@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build();
        ownerService.signup(signupOwner.toEntity());

        HttpSession session = new MockHttpSession();
        ownerService.signin(signupOwner.getEmail(), signupOwner.getPassword(), session);
        assertThat(session.getAttribute("ownerEmail")).isEqualTo(signupOwner.getEmail());
    }

    @Test
    @DisplayName("가게사장 로그인 실패 - 일치하지 않는 이메일")
    void signinFailTest_invalidEmail() {
        OwnerSignupDTO signupOwner = OwnerSignupDTO.builder()
            .email("owner@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build();
        ownerService.signup(signupOwner.toEntity());

        HttpSession session = new MockHttpSession();
        Assertions.assertThrows(DataNotFoundException.class,
            () -> ownerService.signin("fail@gmail.com", signupOwner.getPassword(), session));
    }

    @Test
    @DisplayName("가게사장 로그인 실패 - 일치하지 않는 패스워드")
    void signinFailTest_invalidPassword() {
        OwnerSignupDTO signupOwner = OwnerSignupDTO.builder()
            .email("owner@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build();
        ownerService.signup(signupOwner.toEntity());

        HttpSession session = new MockHttpSession();
        Assertions.assertThrows(DataNotFoundException.class,
            () -> ownerService.signin(signupOwner.getEmail(), "87654321", session));
    }

    @Test
    @DisplayName("가게사장 로그아웃 성공")
    void logoutSuccessTest() {
        OwnerSignupDTO signupOwner = OwnerSignupDTO.builder()
            .email("owner@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build();
        ownerService.signup(signupOwner.toEntity());

        HttpSession session = new MockHttpSession();
        ownerService.signin(signupOwner.getEmail(), signupOwner.getPassword(), session);
        ownerService.logout(session);
        Assertions.assertThrows(IllegalStateException.class,
            () -> session.getAttribute(signupOwner.getEmail()));

    }
}