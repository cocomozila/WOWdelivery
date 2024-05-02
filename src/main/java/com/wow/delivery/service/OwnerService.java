package com.wow.delivery.service;

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

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Transactional
    public void signup(Owner owner) {
        validDuplicateUser(owner);
        ownerRepository.save(owner);
    }

    @Transactional(readOnly = true)
    public void signin(String email, String password, HttpSession session) {
        Owner findOwner = ownerRepository.getOwnerByEmail(email);

        if (!PasswordEncoder.matchesPassword(password, findOwner.getPassword(), findOwner.getSalt())) {
            throw new DataNotFoundException(ErrorCode.MISMATCH_ACCOUNT);
        }
        setSession(findOwner, session);
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    private void setSession(Owner user, HttpSession session) {
        session.setAttribute("ownerEmail", user.getEmail());
        session.setMaxInactiveInterval(60 * 60 * 12);
    }

    private void validDuplicateUser(Owner owner) {
        if (isDuplicateEmail(owner.getEmail())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (isDuplicatePhoneNumber(owner.getPhoneNumber())) {
            throw new InvalidParameterException(ErrorCode.DUPLICATE_PHONE_NUMBER);
        }
    }

    private boolean isDuplicateEmail(String email) {
        return ownerRepository.existsByEmail(email);
    }

    private boolean isDuplicatePhoneNumber(String phoneNumber) {
        return ownerRepository.existsByPhoneNumber(phoneNumber);
    }
}
