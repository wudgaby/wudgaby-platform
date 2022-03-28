package com.wudgaby.platform.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/2/23 0023 17:50
 * @desc :
 */
@UtilityClass
public class IpUtil {
    private static final String LOCAL_IP = "127.0.0.1";
    private static final String LOCAL_REMOTE_HOST = "0:0:0:0:0:0:0:1";

    public static String getIp(HttpServletRequest request){
        if (ObjectUtil.isEmpty(request)) {
            return LOCAL_IP;
        } else {
            String remoteHost = ServletUtil.getClientIP(request);
            return LOCAL_REMOTE_HOST.equals(remoteHost) ? LOCAL_IP : remoteHost;
        }
    }
}
