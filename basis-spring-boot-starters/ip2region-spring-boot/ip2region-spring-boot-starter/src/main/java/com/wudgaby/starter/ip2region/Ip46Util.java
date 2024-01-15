package com.wudgaby.starter.ip2region;

import java.util.regex.Pattern;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 15:26
 * @desc :
 */
public class Ip46Util {
    protected static Pattern IPV4 = Pattern.compile("^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$");
    protected static Pattern IPV6 = Pattern.compile("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    public static boolean isIpv4(String ip4){
        return IPV4.matcher(ip4).matches();
    }

    public static boolean isIpv6(String ip6){
        return IPV6.matcher(ip6).matches();
    }

}
