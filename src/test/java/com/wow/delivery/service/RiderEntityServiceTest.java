package com.wow.delivery.service;

import com.wow.delivery.dto.common.PasswordEncodingDTO;
import com.wow.delivery.dto.rider.RiderSigninDTO;
import com.wow.delivery.dto.rider.RiderSignupDTO;
import com.wow.delivery.entity.RiderEntity;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.RiderRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiderEntityServiceTest {

    @InjectMocks
    private RiderService riderService;

    @Spy
    private RiderRepository riderRepository;

    @AfterEach
    void after() {
        reset(riderRepository);
    }

    @Nested
    @DisplayName("회원가입")
    class Signup {

        @Test
        @DisplayName("성공")
        void signupSuccessTest() {
            // given
            RiderSignupDTO signupRider = RiderSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            given(riderRepository.existsByEmail(signupRider.getEmail()))
                .willReturn(false);
            given(riderRepository.existsByPhoneNumber(signupRider.getPhoneNumber()))
                .willReturn(false);

            // when
            riderService.signup(signupRider);

            // then
            then(riderRepository)
                .should(times(1))
                .save(any());
        }

        @Test
        @DisplayName("실패 - 중복된 이메일")
        void signupFailTest_DuplicateEmail() {
            // given
            RiderSignupDTO signupRider = RiderSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            given(riderRepository.existsByEmail(any()))
                .willReturn(true);

            // when & then
            assertThrows(InvalidParameterException.class,
                () -> riderService.signup(signupRider));
        }

        @Test
        @DisplayName("실패 - 중복된 휴대폰 번호")
        void signupFailTest_DuplicatePhoneNumber() {
            // given
            RiderSignupDTO signupRider = RiderSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011111111")
                .build();

            given(riderRepository.existsByEmail(any()))
                .willReturn(false);
            given(riderRepository.existsByPhoneNumber(any()))
                .willReturn(true);

            // when & then
            assertThrows(InvalidParameterException.class,
                () -> riderService.signup(signupRider));
        }
    }

    @Nested
    @DisplayName("로그인")
    class Signin {

        @Test
        @DisplayName("성공 - 세션에 이메일 등록")
        void signinSuccessTest() {
            // given
            RiderSignupDTO signupDTO = RiderSignupDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .phoneNumber("01011112222")
                .build();

            PasswordEncodingDTO passwordEncoder =
                PasswordEncoder.encodePassword(signupDTO.getPassword());

            RiderEntity rider = RiderEntity.builder()
                .email(signupDTO.getEmail())
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber(signupDTO.getPhoneNumber())
                .build();

            rider.setId(1L);

            RiderSigninDTO signinDTO = RiderSigninDTO.builder()
                .email("test@gmail.com")
                .password("12345678")
                .build();

            HttpSession session = mock(HttpSession.class);

            given(riderRepository.findRiderByEmail(signinDTO.getEmail()))
                .willReturn(Optional.of(rider));

            // when
            riderService.signin(signinDTO, session);

            // then
            then(session)
                .should(times(1))
                .setAttribute(any(), any());
        }

        @Test
        @DisplayName("실패 - 일치하지 않는 이메일")
        void signinFailTest_invalidEmail() {
            // given
            RiderSigninDTO faliSigninDTO = RiderSigninDTO.builder()
                .email("wrong@gmail.com")
                .password("12345678")
                .build();

            HttpSession session = mock(HttpSession.class);

            given(riderRepository.findRiderByEmail(any()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> riderService.signin(faliSigninDTO, session))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("일치하는 계정을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 일치하지 않는 패스워드")
        void signinFailTest_invalidPassword() {
            // given
            RiderSigninDTO faliSigninDTO = RiderSigninDTO.builder()
                .email("seyun@gmail.com")
                .password("wrongwrong")
                .build();

            String password = "12345678";

            PasswordEncodingDTO passwordEncoder =
                PasswordEncoder.encodePassword(password);

            RiderEntity rider = RiderEntity.builder()
                .email("seyun@gmail.com")
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber("01044445555")
                .build();

            HttpSession session = mock(HttpSession.class);

            given(riderRepository.findRiderByEmail(faliSigninDTO.getEmail()))
                .willReturn(Optional.of(rider));

            // when & then
            assertThatThrownBy(() -> riderService.signin(faliSigninDTO, session))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("일치하는 계정을 찾을 수 없습니다.");
        }
    }

}