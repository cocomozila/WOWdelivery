package com.wow.delivery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wow.delivery.dto.user.UserSignupDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    UserSignupDTO userSignupDTO;
    UserSignupDTO duplicateEmailUserSignupDTO;
    UserSignupDTO duplicatePhoneNumberUserSignupDTO;
    UserSignupDTO invalidEmailUserSignupDTO;
    UserSignupDTO invalidPhoneNumberUserSignupDTO;

    @BeforeEach
    void init() {
        userSignupDTO = UserSignupDTO.builder()
            .email("seyun94@naver.com")
            .password("password")
            .phoneNumber("01077778888")
            .build();

        duplicateEmailUserSignupDTO = UserSignupDTO.builder()
            .email("seyun94@naver.com")
            .password("password")
            .phoneNumber("01077889900")
            .build();

        duplicatePhoneNumberUserSignupDTO = UserSignupDTO.builder()
            .email("yun75@naver.com")
            .password("password")
            .phoneNumber("01077778888")
            .build();

        invalidEmailUserSignupDTO = UserSignupDTO.builder()
            .email("@seyun94@naver.com")
            .password("password")
            .phoneNumber("01077778889")
            .build();

        invalidPhoneNumberUserSignupDTO = UserSignupDTO.builder()
            .email("seyun94@naver.com")
            .password("password")
            .phoneNumber("010777788881")
            .build();
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    @DisplayName("유저 회원가입 - 성공")
    @Rollback(false)
    @Order(0)
    void signupSuccessTest() throws Exception {
        mockMvc.perform(post("/user/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(userSignupDTO)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("유저 회원가입 - 실패 - 중복된 이메일")
    void duplicateEmailSignupFailTest() throws Exception {
        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(duplicateEmailUserSignupDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유저 회원가입 - 실패 - 중복된 휴대폰 번호")
    void duplicatePhoneNumberSignupFailTest() throws Exception {
        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(duplicatePhoneNumberUserSignupDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유저 회원가입 - 실패 - 이메일 형식 오류")
    void invalidEmailSignupFailTest() throws Exception {
        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(invalidEmailUserSignupDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유저 회원가입 - 실패 - 휴대폰 번호 형식 오류")
    void invalidPhoneNumberSignupFailTest() throws Exception {
        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(invalidPhoneNumberUserSignupDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

}