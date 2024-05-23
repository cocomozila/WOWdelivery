package com.wow.delivery.service;

import com.wow.delivery.dto.common.PasswordEncodingDTO;
import com.wow.delivery.dto.owner.OwnerSigninDTO;
import com.wow.delivery.dto.owner.OwnerSignupDTO;
import com.wow.delivery.entity.Owner;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.error.exception.InvalidParameterException;
import com.wow.delivery.repository.OwnerRepository;
import com.wow.delivery.util.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Transactional
    public void signup(OwnerSignupDTO ownerSignupDTO) {
        validDuplicateUser(ownerSignupDTO);
        PasswordEncodingDTO passwordEncoder
            = PasswordEncoder.encodePassword(ownerSignupDTO.getPassword());
        Owner owner = Owner.builder()
                .email(ownerSignupDTO.getEmail())
                .password(passwordEncoder.getEncodePassword())
                .salt(passwordEncoder.getSalt())
                .phoneNumber(ownerSignupDTO.getPhoneNumber())
                .build();
        ownerRepository.save(owner);
    }

    @Transactional(readOnly = true)
    public void signin(OwnerSigninDTO ownerSigninDTO, HttpSession session) {
        Owner findOwner = ownerRepository.getByEmail(ownerSigninDTO.getEmail());
        if (!PasswordEncoder.matchesPassword(ownerSigninDTO.getPassword(), findOwner.getPassword(), findOwner.getSalt())) {
            throw new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "일치하는 계정을 찾을 수 없습니다.");
        }
        setSession(findOwner, session);
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    private void setSession(Owner owner, HttpSession session) {
        session.setAttribute(UUID.randomUUID().toString(), owner);
        session.setMaxInactiveInterval(60 * 30);
    }

    private void validDuplicateUser(OwnerSignupDTO ownerSignupDTO) {
        if (isDuplicateEmail(ownerSignupDTO.getEmail())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_DATA, "중복된 이메일 입니다.");
        }
        if (isDuplicatePhoneNumber(ownerSignupDTO.getPhoneNumber())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_DATA, "중복된 휴대폰 번호 입니다.");
        }
    }

    private boolean isDuplicateEmail(String email) {
        return ownerRepository.existsByEmail(email);
    }

    private boolean isDuplicatePhoneNumber(String phoneNumber) {
        return ownerRepository.existsByPhoneNumber(phoneNumber);
    }
}
