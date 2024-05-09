package com.wow.delivery.service;

import com.wow.delivery.dto.user.UserSignupDTO;
import com.wow.delivery.entity.User;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Spy
    private UserRepository userRepository;

    @Nested
    @DisplayName("회원가입")
    class Signup {

        @Test
        @DisplayName("성공")
        void signupSuccessTest() {
            // given
            User signupUser = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build()
                .toEntity();

            given(userRepository.existsByEmail(any()))
                .willReturn(false);
            given(userRepository.existsByPhoneNumber(any()))
                .willReturn(false);

            // when
            userService.signup(signupUser);

            // then
            then(userRepository)
                .should(times(1))
                .save(any());
        }

        @Test
        @DisplayName("실패 - 중복된 이메일")
        void signupFailTest_DuplicateEmail() {
            // given
            User signupUser = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build()
                .toEntity();

            given(userRepository.existsByEmail(any()))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.signup(signupUser))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("중복된 이메일 입니다.");
        }

        @Test
        @DisplayName("실패 - 중복된 휴대폰 번호")
        void signupFailTest_DuplicatePhoneNumber() {
            // given
            User signupUser = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build()
                .toEntity();

            given(userRepository.existsByEmail(any()))
                .willReturn(false);
            given(userRepository.existsByPhoneNumber(any()))
                .willReturn(true);

            // when & then
            assertThrows(InvalidParameterException.class,
                () -> userService.signup(signupUser));
        }
    }

    @Nested
    @DisplayName("로그인")
    class Signin {

        @Test
        @DisplayName("성공 - 세션에 이메일 등록")
        void signinSuccessTest() {
            // given
            UserSignupDTO signupUser = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            User user = signupUser.toEntity();
            HttpSession session = mock(HttpSession.class);

            given(userRepository.findUserByEmail(signupUser.getEmail()))
                .willReturn(Optional.of(user));

            doNothing()
                .when(session)
                .setAttribute("userEmail", signupUser.getEmail());

            // when
            userService.signin(signupUser.getEmail(), signupUser.getPassword(), session);

            // then
            then(session)
                .should(times(1))
                .setAttribute("userEmail", signupUser.getEmail());
        }

        @Test
        @DisplayName("실패 - 일치하지 않는 이메일")
        void signinFailTest_invalidEmail() {
            // given
            UserSignupDTO signupUser = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            String wrongSigninEmail = "wrong@gmail.com";
            HttpSession session = mock(HttpSession.class);

            given(userRepository.findUserByEmail(any()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.signin(wrongSigninEmail, signupUser.getPassword(), session))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("일치하는 계정을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 일치하지 않는 패스워드")
        void signinFailTest_invalidPassword() {
            // given
            UserSignupDTO signupUser = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            String wrongPassword = "wrongwrong";

            User user = signupUser.toEntity();
            HttpSession session = mock(HttpSession.class);

            given(userRepository.findUserByEmail(signupUser.getEmail()))
                .willReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() -> userService.signin(signupUser.getEmail(), wrongPassword, session))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("일치하는 계정을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("로그아웃 성공")
        void logoutSuccessTest() {
            // given
            HttpSession session = spy(HttpSession.class);
            session.setAttribute("userEmail", "test@gmail.com");

            // when
            userService.logout(session);

            // then
            then(session).should(times(1)).invalidate();
            assertThat(session.getAttribute("userEmail")).isNull();
        }
    }
}