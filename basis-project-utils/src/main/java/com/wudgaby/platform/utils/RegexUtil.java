package com.wudgaby.platform.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则.
 *
 * @author KimZing - kimzing@163.com
 * @since 2018-08-07 02:02
 */
public class RegexUtil {

    /**
     * 手机号+86-10-13523458056,  +86-13523458056 ，10-13523458056 ，13523458056 国家代码和区号选填
     */
    public static final Pattern MOBILE = Pattern.compile("^(\\+\\d{2}-)?(\\d{2,3}-)?([1][3,4,5,6,7,8]\\d{9})$");

    /**
     * 固定电话+86-010-40020020，010-40020020.国家代码选填
     */
    public static final Pattern TELEPHONE = Pattern.compile("^(\\+\\d{2}-)?0\\d{2,3}-\\d{7,8}$");

    /**
     * 汉字
     */
    public static final Pattern CHINESE = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 邮政编码
     */
    public static final Pattern  POSTALCODE = Pattern.compile("[1-9]\\d{5}(?!\\d)");

    /**
     * 邮箱
     */
    public static final Pattern EMAIL =
            Pattern.compile("^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$");

    /**
     * IP地址
     */
    public static final Pattern IP = Pattern.compile("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)");
    //((1[0-9][0-9]\.)|(2[0-4][0-9]\.)|(25[0-5]\.)|([1-9][0-9]\.)|([0-9]\.)){3}((1[0-9][0-9])|(2[0-4][0-9])|(25[0-5])|([1-9][0-9])|([0-9]))

    /**
     * url
     */
    public static final Pattern URL = Pattern.compile("(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?");

    /**
     * 身份证
     */
    public static final Pattern ID_NUMBER = Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");

    /**
     * `~!@#$%^&*()-_+={[}]|\:;"',.>/?·！@#￥%……&*（）——+【】；：’‘“”，《。》、？
     */
    public static final String ALL_SYMBOL = "[\\pP\\p{Punct}￥]";

    /**
     * 校验邮编.
     *
     * @param code
     * @return boolean
     *
     */
    public static boolean isPostalCode(String code) {
        if (null == code || "".equals(code)) {
            return false;
        }
        Matcher matcher = POSTALCODE.matcher(code);
        return matcher.matches();
    }

    /**
     * 校验手机号是否正确.
     *
     * @param mobile
     * @return isMobile
     */
    public static boolean isMobile(String mobile) {
        if (null == mobile || "".equals(mobile)) {
            return false;
        }
        Matcher m = MOBILE.matcher(mobile);
        return m.matches();
    }

    /**
     * 校验固话是否正确.
     *
     * @param telephone
     * @return isTelephone
     */
    public static boolean isTelephone(String telephone) {
        if (null == telephone || "".equals(telephone)) {
            return false;
        }
        Matcher m = TELEPHONE.matcher(telephone);
        return m.matches();
    }

    /**
     * 校验邮箱是否正确.
     *
     * @param email
     * @return isEmail
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }
        Matcher m = EMAIL.matcher(email);
        return m.matches();
    }

    /**
     * 校验是否是整数.
     *
     * @param value
     * @return isInteger
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断是否含有特殊字符.
     *
     * @param text
     * @return boolean true,通过，false，没通过
     */
    public static boolean hasSpecialChar(String text) {
        if (null == text || "".equals(text)) {
            return true;
        }
        String reg = "[a-z]*[A-Z]*\\d*-*_*\\s*";
        if (text.replaceAll(reg, "").length() == 0) {
            // 如果不包含特殊字符
            return false;
        }
        return true;
    }

    /**
     * 判断是否正整数.
     *
     * @param number 数字
     * @return boolean true,通过，false，没通过
     */
    public static boolean isNumber(String number) {
        if (null == number || "".equals(number)) {
            return false;
        }
        String regex = "[0-9]*";
        return number.matches(regex);
    }

    /**
     * 判断是否是正确的IP地址.
     *
     * @param ip
     * @return boolean true,通过，false，没通过
     */
    public static boolean isIp(String ip) {
        if (null == ip || "".equals(ip)) {
            return false;
        }
        Matcher m = IP.matcher(ip);
        return m.matches();
    }

    /**
     * 判断是否是正确的IP地址.
     *
     * @param url
     * @return boolean true,通过，false，没通过
     */
    public static boolean isURL(String url) {
        if (null == url || "".equals(url)) {
            return false;
        }
        Matcher m = URL.matcher(url);
        return m.matches();
    }

    /**
     * 判断是否是正确的身份号
     * @param idNumber
     * @return
     */
    public static boolean isIdNumber(String idNumber) {
        if (null == idNumber || "".equals(idNumber)) {
            return false;
        }
        Matcher matcher = ID_NUMBER.matcher(idNumber);
        return matcher.matches();
    }

    /**
     * 判断是否含有中文，仅适合中国汉字，不包括标点.
     *
     * @param text
     * @return boolean true,通过，false，没通过
     */
    public static boolean isChinese(String text) {
        if (null == text || "".equals(text)) {
            return false;
        }
        Matcher m = CHINESE.matcher(text);
        return m.find();
    }

    /**
     * 判断字符串str是否符合正则表达式reg.
     *
     * @param str 需要处理的字符串
     * @param reg 正则
     * @return 是否匹配
     */
    public static boolean isMatche(String str, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

}