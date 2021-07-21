package com.wudgaby.platform.sso.core.helper;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * @ClassName : PathHelper
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/24 11:49
 * @Desc :
 */
@UtilityClass
public class PathHelper {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    public static boolean excludedPath(Collection<String> excludedPaths, String servletPath){
        if (CollectionUtils.isEmpty(excludedPaths)) {
            return false;
        }
        for (String excludedPath: excludedPaths) {
            String uriPattern = excludedPath.trim();
            if (antPathMatcher.match(uriPattern, servletPath)) {
                return true;
            }
        }
        return false;
    }
}
