package com.wudgaby.platform.sso.core.helper;

import com.google.common.base.Splitter;
import com.wudgaby.platform.sso.core.vo.SessionIdVo;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @ClassName : SsoSessionIdHelper
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/24 9:39
 * @Desc :   
 */
@UtilityClass
public class SsoSessionIdHelper {
    private static final String SEPARATOR = "_";
    public static SessionIdVo parse(String sessionId){
        if(StringUtils.isBlank(sessionId)){
            return null;
        }

        List<String> result = Splitter.on(SEPARATOR).omitEmptyStrings().trimResults().splitToList(sessionId);
        if(CollectionUtils.size(result) != 2){
            return null;
        }

        SessionIdVo sessionIdVo = new SessionIdVo()
                .setStoreKey(result.get(0))
                .setVersion(result.get(1));
        return sessionIdVo;
    }

    public static String buildSessionId(SsoUserVo ssoUserVo){
        return ssoUserVo.getUserId() + SEPARATOR + ssoUserVo.getVersion();
    }
}
