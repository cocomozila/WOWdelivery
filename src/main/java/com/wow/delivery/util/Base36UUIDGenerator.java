package com.wow.delivery.util;

import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.InvalidParameterException;

import java.math.BigInteger;
import java.util.UUID;

public class Base36UUIDGenerator {

    public static String generate(int digits) {
        validParameter(digits);
        String str = convertUUIDToBase36(UUID.randomUUID());
        return str.substring(str.length() - digits);
    }

    private static String convertUUIDToBase36(UUID uuid) {
        String uuidStr = uuid.toString().replace("-", "");
        BigInteger bigInt = new BigInteger(uuidStr, 16);
        return bigInt.toString(36).toUpperCase();
    }

    private static void validParameter(int digits) {
        if (digits < 1 || digits > 9) {
            throw new InvalidParameterException(ErrorCode.INVALID_PARAMETER, "1 이상 9 이하의 정수만 가능합니다.");
        }
    }
}
