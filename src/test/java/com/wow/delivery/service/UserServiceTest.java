package com.wow.delivery.service;

import com.wow.delivery.dto.common.PasswordEncodingDTO;
import com.wow.delivery.dto.user.UserSigninDTO;
import com.wow.delivery.dto.user.UserSignupDTO;
import com.wow.delivery.entity.UserEntity;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.UserRepository;
import com.wow.delivery.service.user.UserService;
import com.wow.delivery.util.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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

    @AfterEach
    void after() {
        reset(userRepository);
    }

    @Nested
    @DisplayName("회원가입")
    class Signup {

        @Test
        @DisplayName("성공")
        void signupSuccessTest() {
            // given
            UserSignupDTO signupUser = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            given(userRepository.existsByEmail(signupUser.getEmail()))
                .willReturn(false);
            given(userRepository.existsByPhoneNumber(signupUser.getPhoneNumber()))
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
            UserSignupDTO signupUser = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            given(userRepository.existsByEmail(any()))
                .willReturn(true);

            // when & then
            assertThrows(InvalidParameterException.class,
                () -> userService.signup(signupUser));
        }

        @Test
        @DisplayName("실패 - 중복된 휴대폰 번호")
        void signupFailTest_DuplicatePhoneNumber() {
            // given
            UserSignupDTO signupUser = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

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
            UserSignupDTO signupDTO = UserSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011112222")
                .build();

            PasswordEncodingDTO passwordEncoder =
                PasswordEncoder.encodePassword(signupDTO.getPassword());

            UserEntity userEntity = UserEntity.builder()
                .email(signupDTO.getEmail())
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber(signupDTO.getPhoneNumber())
                .build();

            userEntity.setId(1L);

            UserSigninDTO signinDTO = UserSigninDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .build();

            HttpSession session = mock(HttpSession.class);

            given(userRepository.findUserByEmail(signinDTO.getEmail()))
                .willReturn(Optional.of(userEntity));

            // when
            userService.signin(signinDTO, session);

            // then
            then(session)
                .should(times(1))
                .setAttribute(any(), any());
        }

        @Test
        @DisplayName("실패 - 일치하지 않는 이메일")
        void signinFailTest_invalidEmail() {
            // given
            UserSigninDTO faliSigninDTO = UserSigninDTO.builder()
                .email("wrong@gmail.com")
                .password("12345678")
                .build();

            HttpSession session = mock(HttpSession.class);

            given(userRepository.findUserByEmail(any()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.signin(faliSigninDTO, session))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("일치하는 계정을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 일치하지 않는 패스워드")
        void signinFailTest_invalidPassword() {
            // given
            UserSigninDTO faliSigninDTO = UserSigninDTO.builder()
                .email("seyun@gmail.com")
                .password("wrongwrong")
                .build();

            String password = "12345678";

            PasswordEncodingDTO passwordEncoder =
                PasswordEncoder.encodePassword(password);

            UserEntity userEntity = UserEntity.builder()
                .email("seyun@gmail.com")
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber("01044445555")
                .build();

            HttpSession session = mock(HttpSession.class);

            given(userRepository.findUserByEmail(faliSigninDTO.getEmail()))
                .willReturn(Optional.of(userEntity));

            // when & then
            assertThatThrownBy(() -> userService.signin(faliSigninDTO, session))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("일치하는 계정을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("로그아웃 성공")
        void logoutSuccessTest() {
            // given
            String password = "12345678";

            PasswordEncodingDTO passwordEncoder =
                PasswordEncoder.encodePassword(password);

            UserEntity userEntity = UserEntity.builder()
                .email("seyun@gmail.com")
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber("01044445555")
                .build();

            String uuid = UUID.randomUUID().toString();
            HttpSession session = spy(HttpSession.class);
            session.setAttribute(uuid, userEntity);

            // when
            userService.logout(session);

            // then
            then(session)
                .should(times(1))
                .invalidate();
            assertThat(session.getAttribute(uuid))
                .isNull();
        }
    }
}