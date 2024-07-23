package com.wow.delivery.service;

import com.wow.delivery.dto.common.PasswordEncodingDTO;
import com.wow.delivery.dto.owner.OwnerSigninDTO;
import com.wow.delivery.dto.owner.OwnerSignupDTO;
import com.wow.delivery.entity.OwnerEntity;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.OwnerRepository;
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
public class OwnerServiceTest {

    @InjectMocks
    private OwnerService ownerService;

    @Spy
    private OwnerRepository ownerRepository;

    @AfterEach
    void after() {
        reset(ownerRepository);
    }

    @Nested
    @DisplayName("회원가입")
    class Signup {

        @Test
        @DisplayName("성공")
        void signupSuccessTest() {
            // given
            OwnerSignupDTO signupOwner = OwnerSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            given(ownerRepository.existsByEmail(signupOwner.getEmail()))
                .willReturn(false);
            given(ownerRepository.existsByPhoneNumber(signupOwner.getPhoneNumber()))
                .willReturn(false);

            // when
            ownerService.signup(signupOwner);

            // then
            then(ownerRepository)
                .should(times(1))
                .save(any());
        }

        @Test
        @DisplayName("실패 - 중복된 이메일")
        void signupFailTest_DuplicateEmail() {
            // given
            OwnerSignupDTO signupOwner = OwnerSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            given(ownerRepository.existsByEmail(any()))
                .willReturn(true);


            // when & then
            assertThrows(InvalidParameterException.class,
                () -> ownerService.signup(signupOwner));
        }

        @Test
        @DisplayName("실패 - 중복된 휴대폰 번호")
        void signupFailTest_DuplicatePhoneNumber() {
            // given
            OwnerSignupDTO signupOwner = OwnerSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            given(ownerRepository.existsByEmail(any()))
                .willReturn(false);
            given(ownerRepository.existsByPhoneNumber(any()))
                .willReturn(true);

            // when & then
            assertThrows(InvalidParameterException.class,
                () -> ownerService.signup(signupOwner));
        }
    }

    @Nested
    @DisplayName("로그인")
    class Signin {

        @Test
        @DisplayName("성공 - 세션에 이메일 등록")
        void signinSuccessTest() {
            // given
            OwnerSignupDTO signupOwner = OwnerSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011112222")
                .build();

            PasswordEncodingDTO passwordEncoder =
                PasswordEncoder.encodePassword(signupOwner.getPassword());

            OwnerEntity ownerEntity = OwnerEntity.builder()
                .email(signupOwner.getEmail())
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber(signupOwner.getPhoneNumber())
                .build();

            ownerEntity.setId(1L);

            OwnerSigninDTO signinDTO = OwnerSigninDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .build();

            HttpSession session = mock(HttpSession.class);

            given(ownerRepository.findUserByEmail(signinDTO.getEmail()))
                .willReturn(Optional.of(ownerEntity));

            // when
            ownerService.signin(signinDTO, session);

            // then
            then(session)
                .should(times(1))
                .setAttribute(any(), any());
        }

        @Test
        @DisplayName("실패 - 일치하지 않는 이메일")
        void signinFailTest_invalidEmail() {
            // given
            OwnerSigninDTO faliSigninDTO = OwnerSigninDTO.builder()
                .email("wrong@gmail.com")
                .password("12345678")
                .build();

            HttpSession session = mock(HttpSession.class);

            given(ownerRepository.findUserByEmail(any()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> ownerService.signin(faliSigninDTO, session))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("일치하는 계정을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 일치하지 않는 패스워드")
        void signinFailTest_invalidPassword() {
            // given
            OwnerSigninDTO faliSigninDTO = OwnerSigninDTO.builder()
                .email("seyun@gmail.com")
                .password("wrongwrong")
                .build();

            String password = "12345678";

            PasswordEncodingDTO passwordEncoder =
                PasswordEncoder.encodePassword(password);

            OwnerEntity ownerEntity = OwnerEntity.builder()
                .email("seyun@gmail.com")
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber("01044445555")
                .build();

            HttpSession session = mock(HttpSession.class);

            given(ownerRepository.findUserByEmail(faliSigninDTO.getEmail()))
                .willReturn(Optional.of(ownerEntity));

            // when & then
            assertThatThrownBy(() -> ownerService.signin(faliSigninDTO, session))
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

            OwnerEntity ownerEntity = OwnerEntity.builder()
                .email("seyun@gmail.com")
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber("01044445555")
                .build();

            String uuid = UUID.randomUUID().toString();
            HttpSession session = spy(HttpSession.class);
            session.setAttribute(uuid, ownerEntity);

            // when
            ownerService.logout(session);

            // then
            then(session)
                .should(times(1))
                .invalidate();
            assertThat(session.getAttribute(uuid))
                .isNull();
        }
    }
}