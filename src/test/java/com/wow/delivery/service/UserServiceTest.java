package com.wow.delivery.service;

import com.wow.delivery.dto.user.UserSignupDTO;
import com.wow.delivery.entity.User;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void after() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccessTest() {
        User signupUser = UserSignupDTO.builder()
            .email("test@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build().toEntity();

        userService.signup(signupUser);
        User findByUser = userRepository.getUserByEmail(signupUser.getEmail());
        assertThat(findByUser.getId()).isNotNull();
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void signupFailTest_DuplicateEmail() {
        User signupUser = UserSignupDTO.builder()
            .email("test@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build().toEntity();

        userService.signup(signupUser);

        User duplicateEmailSignupUser = UserSignupDTO.builder()
            .email("test@gmail.com")
            .password("12345678")
            .phoneNumber("01011113333")
            .build().toEntity();

        Assertions.assertThrows(InvalidParameterException.class,
            () -> userService.signup(duplicateEmailSignupUser));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 휴대폰 번호")
    void signupFailTest_DuplicatePhoneNumber() {
        User signupUser = UserSignupDTO.builder()
            .email("test@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build().toEntity();

        userService.signup(signupUser);

        User duplicatePhoneNumberSignupUser = UserSignupDTO.builder()
            .email("basic@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build().toEntity();

        Assertions.assertThrows(InvalidParameterException.class,
            () -> userService.signup(duplicatePhoneNumberSignupUser));
    }
}