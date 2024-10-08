package com.wow.delivery.service.user;

import com.wow.delivery.dto.common.PasswordEncodingDTO;
import com.wow.delivery.dto.user.UserSigninDTO;
import com.wow.delivery.dto.user.UserSigninResponse;
import com.wow.delivery.dto.user.UserSignupDTO;
import com.wow.delivery.entity.UserEntity;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.UserRepository;
import com.wow.delivery.util.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(UserSignupDTO userSignupDTO) {
        validDuplicateUser(userSignupDTO);
        PasswordEncodingDTO passwordEncoder =
            PasswordEncoder.encodePassword(userSignupDTO.getPassword());
        UserEntity userEntity = UserEntity.builder()
                .email(userSignupDTO.getEmail())
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber(userSignupDTO.getPhoneNumber())
                .build();
        userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public UserSigninResponse signin(UserSigninDTO userSigninDTO, HttpSession session) {
        UserEntity userEntity = userRepository.getByEmail(userSigninDTO.getEmail());
        if (!PasswordEncoder.matchesPassword(userSigninDTO.getPassword(), userEntity.getPassword(), userEntity.getSalt())) {
            throw new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "일치하는 계정을 찾을 수 없습니다.");
        }
        setSession(userEntity.getIdOrThrow() , session);
        return UserSigninResponse.builder()
            .id(userEntity.getIdOrThrow())
            .build();
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    private void setSession(Long id, HttpSession session) {
        session.setAttribute(UUID.randomUUID().toString(), id);
        session.setMaxInactiveInterval(60 * 30);
    }

    private void validDuplicateUser(UserSignupDTO userSignupDTO) {
        if (isDuplicateEmail(userSignupDTO.getEmail())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_DATA, "중복된 이메일 입니다.");
        }
        if (isDuplicatePhoneNumber(userSignupDTO.getPhoneNumber())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_DATA, "중복된 휴대폰 번호 입니다.");
        }
    }

    private boolean isDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isDuplicatePhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
