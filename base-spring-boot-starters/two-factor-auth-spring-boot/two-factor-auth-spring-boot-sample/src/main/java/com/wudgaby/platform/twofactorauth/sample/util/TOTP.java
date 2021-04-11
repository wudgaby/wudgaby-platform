package com.wudgaby.platform.twofactorauth.sample.util;

import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName : TOTP
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/9 23:31
 * @Desc :   TODO
 */
@UtilityClass
public class TOTP {

    private static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    static byte[] hexStr2Bytes(String hex) {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        System.arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }

    private static final int[] DIGITS_POWER
            // 0 1 2 3 4 5 6 7 8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    public static String generateTOTP(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA1");
    }

    public static String generateTOTP256(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA256");
    }

    public static String generateTOTP512(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA512");
    }

    public static String generateTOTP(String key, String time, String returnDigits, String crypto) {
        return generateTOTP(hexStr2Bytes(key), time, returnDigits, crypto);
    }

    public static String generateTOTP(byte[] key, String time, String returnDigits, String crypto) {
        int codeDigits = Integer.decode(returnDigits);

        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        StringBuilder timeBuilder = new StringBuilder(time);
        while (timeBuilder.length() < 16)
            timeBuilder.insert(0, "0");
        time = timeBuilder.toString();

        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(time);
        byte[] hash = hmac_sha(crypto, key, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        StringBuilder result = new StringBuilder(Integer.toString(otp));
        while (result.length() < codeDigits) {
            result.insert(0, "0");
        }
        return result.toString();
    }

    public static String generateTOTP(byte[] key, long sec30OffSet) {
        long nowIn30Sec = System.currentTimeMillis() / 1000 / 30;
        return generateTOTP(key, Long.toHexString(nowIn30Sec + sec30OffSet), "6", "HmacSHA1");
    }

    public static String generateTOTP(byte[] key) {
        return generateTOTP(key, 0L);
    }

    public static String generateTOTP(String base32Key) {
        try {
            return generateTOTP(Base32String.decode(base32Key), 0L);
        } catch (Base32String.DecodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> generateTOTP3(byte[] key) {
        return Arrays.asList(generateTOTP(key, -1L), generateTOTP(key, 0L), generateTOTP(key, 1L));
    }

    public static List<String> generateTOTP3(String base32Key) {
        try {
            return generateTOTP3(Base32String.decode(base32Key));
        } catch (Base32String.DecodingException e) {
            throw new RuntimeException(e);
        }
    }
}