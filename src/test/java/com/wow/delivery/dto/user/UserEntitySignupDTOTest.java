package com.wow.delivery.dto.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserEntitySignupDTOTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    @DisplayName("Binding 성공")
    void bindingSuccessTest() throws Exception {
        UserSignupDTO invalidUserSignupDTO = UserSignupDTO.builder()
            .email("test@gmail.com")
            .password("12345678")
            .phoneNumber("01012345678")
            .build();

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(invalidUserSignupDTO)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Binding 실패 - 이메일 형식 오류")
    void bindingFailTest_invalidEmail() throws Exception {
        UserSignupDTO invalidUserSignupDTO = UserSignupDTO.builder()
            .email("!@test@gmail.com")
            .password("12345678")
            .phoneNumber("01011111111")
            .build();

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(invalidUserSignupDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("INVALID_PARAMETER"))
            .andExpect(jsonPath("$.errors[0].field").value("email"));
    }

    @Test
    @DisplayName("Binding 실패 - 비밀번호 형식 오류")
    void bindingFailTest_invalidPassword() throws Exception {
        UserSignupDTO invalidUserSignupDTO = UserSignupDTO.builder()
            .email("test@gmail.com")
            .password("1")
            .phoneNumber("01011111111")
            .build();

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(invalidUserSignupDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("INVALID_PARAMETER"))
            .andExpect(jsonPath("$.errors[0].field").value("password"));
    }

    @Test
    @DisplayName("Binding 실패 - 휴대폰 번호 형식 오류")
    void bindingFailTest_invalidPhoneNumber() throws Exception {
        UserSignupDTO invalidUserSignupDTO = UserSignupDTO.builder()
            .email("test@gmail.com")
            .password("12345678")
            .phoneNumber("010111111110")
            .build();

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(invalidUserSignupDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("INVALID_PARAMETER"))
            .andExpect(jsonPath("$.errors[0].field").value("phoneNumber"));
    }
}