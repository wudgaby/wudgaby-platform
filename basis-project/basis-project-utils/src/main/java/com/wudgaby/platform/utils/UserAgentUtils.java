package com.wudgaby.platform.utils;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * @ClassName : UserAgentUtils
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/1 11:17
 * @Desc :
 */
@UtilityClass
public class UserAgentUtils {
    private static final List<String> COMMON_MOBILE_LIST = Lists.newArrayList();
    private static final List<String> COMMON_DESKTOP_LIST = Lists.newArrayList();

    static {
        COMMON_DESKTOP_LIST.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        COMMON_DESKTOP_LIST.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:69.0) Gecko/20100101 Firefox/69.0");
        COMMON_DESKTOP_LIST.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
        COMMON_DESKTOP_LIST.add("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        COMMON_DESKTOP_LIST.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        COMMON_DESKTOP_LIST.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");
        COMMON_DESKTOP_LIST.add("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11");
        COMMON_DESKTOP_LIST.add("Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11");

        COMMON_MOBILE_LIST.add("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");
        COMMON_MOBILE_LIST.add("Mozilla/5.0 (iPod; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");
        COMMON_MOBILE_LIST.add("Mozilla/5.0 (iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");
        COMMON_MOBILE_LIST.add("Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        COMMON_MOBILE_LIST.add("Opera/9.80 (Android 2.3.4; Linux; Opera Mobi/build-1107180945; U; en-GB) Presto/2.8.149 Version/11.10");
        COMMON_MOBILE_LIST.add("Mozilla/5.0 (Linux; U; Android 3.0; en-us; Xoom Build/HRI39) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13");
        COMMON_MOBILE_LIST.add("Mozilla/5.0 (BlackBerry; U; BlackBerry 9800; en) AppleWebKit/534.1+ (KHTML, like Gecko) Version/6.0.0.337 Mobile Safari/534.1+");
        COMMON_MOBILE_LIST.add("Mozilla/5.0 (SymbianOS/9.4; Series60/5.0 NokiaN97-1/20.0.019; Profile/MIDP-2.1 Configuration/CLDC-1.1) AppleWebKit/525 (KHTML, like Gecko) BrowserNG/7.1.18124");
    }

    public static String getUserAgent(int index){
        return COMMON_DESKTOP_LIST.get(index);
    }

    public static String randomDesktopUserAgent(){
        return COMMON_DESKTOP_LIST.get(RandomUtil.randomRange(0, COMMON_DESKTOP_LIST.size()));
    }

    public static String randomMobileUserAgent(){
        return COMMON_MOBILE_LIST.get(RandomUtil.randomRange(0, COMMON_MOBILE_LIST.size()));
    }
}
