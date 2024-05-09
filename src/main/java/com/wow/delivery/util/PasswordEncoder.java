package com.wow.delivery.util;

import com.wow.delivery.dto.common.PasswordEncodingDTO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncoder {

    private static final int SALT_LENGTH = 16;

    public static PasswordEncodingDTO encodePassword(String rawPassword) {
        String salt = generateSalt();
        byte[] hashedPassword = hashPassword(rawPassword, salt);
        return new PasswordEncodingDTO(salt, Base64.getEncoder().encodeToString(hashedPassword));
    }

    public static boolean matchesPassword(String rawPassword, String encodedPassword, String salt) {
        byte[] decodedPassword = Base64.getDecoder().decode(encodedPassword);
        System.arraycopy(decodedPassword, 0, salt.getBytes(), 0, SALT_LENGTH);
        byte[] hashedPassword = hashPassword(rawPassword, salt);
        return MessageDigest.isEqual(hashedPassword, decodedPassword);
    }

    private static byte[] hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String newPassword = salt + password;
            digest.update(newPassword.getBytes());
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("패스워드를 해싱하는 것을 실패했습니다.", e);
        }
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
