package com.wudgaby.platform.utils;

import lombok.experimental.UtilityClass;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @ClassName : JavaBase64Util
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/14 15:06
 * @Desc :   TODO
 */
@UtilityClass
public class JavaBase64Util {
    private static final String UTF_8 = "UTF-8";
    private static Base64.Encoder encoder;
    private static Base64.Decoder decoder;

    /**
     * 即为安全的编码方式，替换“+” “/” “-”为“_”
     */
    private static Base64.Encoder urlEncoder;
    private static Base64.Decoder urlDecoder;

    static {
        encoder = Base64.getEncoder();
        urlEncoder = Base64.getUrlEncoder();
        decoder = Base64.getDecoder();
        urlDecoder = Base64.getUrlDecoder();
    }

    public static byte[] encode(byte[] bytes) {
        return encoder.encode(bytes);
    }

    public static String encode(String string) {
        byte[] encode = encode(string.getBytes());
        try {
            return new String(encode, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String encode2String(byte[] bytes) {
        return encoder.encodeToString(bytes);
    }
    public static byte[] encode2Byte(String string) {
        return encode(string.getBytes());
    }

    public static byte[] urlEncode(byte[] bytes) {
        return urlEncoder.encode(bytes);
    }
    public static String urlEncode(String string) {
        byte[] encode = urlEncode(string.getBytes());
        try {
            return new String(encode, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String urlEncode2String(byte[] bytes) {
        return urlEncoder.encodeToString(bytes);
    }
    public static byte[] urlEncode2Byte(String string) {
        return urlEncode(string.getBytes());
    }
    public static byte[] decode(byte[] bytes) {
        return decoder.decode(bytes);
    }
    public static byte[] decode2Byte(String string) {
        return decoder.decode(string.getBytes());
    }
    public static String decode2String(byte[] bytes) {
        try {
            return new String(decoder.decode(bytes),UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String decode(String string) {
        byte[] decode = decode(string.getBytes());
        try {
            return new String(decode, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] urlDecode(byte[] bytes) {
        return urlDecoder.decode(bytes);
    }
    public static byte[] urlDecode2Byte(String string) {
        return urlDecode(string.getBytes());
    }
    public static String urlDecode2String(byte[] bytes) {
        try {
            return new String(urlDecode(bytes),UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String urlDecode(String string) {
        byte[] decode = urlDecode(string.getBytes());
        try {
            return new String(decode, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
