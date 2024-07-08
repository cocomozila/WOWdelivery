package com.wow.delivery.service;

import com.wow.delivery.dto.common.PasswordEncodingDTO;
import com.wow.delivery.dto.rider.RiderSigninDTO;
import com.wow.delivery.dto.rider.RiderSigninResponse;
import com.wow.delivery.dto.rider.RiderSignupDTO;
import com.wow.delivery.entity.Rider;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.RiderRepository;
import com.wow.delivery.util.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository riderRepository;

    @Transactional
    public void signup(RiderSignupDTO riderSignupDTO) {
        validDuplicateRider(riderSignupDTO);
        PasswordEncodingDTO passwordEncoder =
            PasswordEncoder.encodePassword(riderSignupDTO.getPassword());
        Rider rider = Rider.builder()
            .email(riderSignupDTO.getEmail())
            .password(passwordEncoder.getEncodePassword())
            .salt(passwordEncoder.getSalt())
            .phoneNumber(riderSignupDTO.getPhoneNumber())
            .build();
        riderRepository.save(rider);
    }

    @Transactional(readOnly = true)
    public RiderSigninResponse signin(RiderSigninDTO userSigninDTO, HttpSession session) {
        Rider rider = riderRepository.getByEmail(userSigninDTO.getEmail());
        if (!PasswordEncoder.matchesPassword(userSigninDTO.getPassword(), rider.getPassword(), rider.getSalt())) {
            throw new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "일치하는 계정을 찾을 수 없습니다.");
        }
        setSession(rider.getIdOrThrow(), session);
        return RiderSigninResponse.builder()
            .id(rider.getIdOrThrow())
            .build();
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    private void setSession(Long id, HttpSession session) {
        session.setAttribute(UUID.randomUUID().toString(), id);
        session.setMaxInactiveInterval(60 * 30);
    }

    private void validDuplicateRider(RiderSignupDTO riderSignupDTO) {
        if (isDuplicateEmail(riderSignupDTO.getEmail())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_DATA, "중복된 이메일 입니다.");
        }
        if (isDuplicatePhoneNumber(riderSignupDTO.getPhoneNumber())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_DATA, "중복된 휴대폰 번호 입니다.");
        }
    }

    private boolean isDuplicateEmail(String email) {
        return riderRepository.existsByEmail(email);
    }

    private boolean isDuplicatePhoneNumber(String phoneNumber) {
        return riderRepository.existsByPhoneNumber(phoneNumber);
    }
}
