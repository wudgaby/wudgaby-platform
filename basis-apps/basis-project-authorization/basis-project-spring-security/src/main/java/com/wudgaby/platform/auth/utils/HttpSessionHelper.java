package com.wudgaby.platform.auth.utils;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName : HttpSessionService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/7/1 18:03
 * @Desc :   
 */
@AllArgsConstructor
public class HttpSessionHelper {
    private FindByIndexNameSessionRepository sessionRepository;

    /**
     * 踢出系统
     * @param accountSet 账号列表
     */
    public void invalidateSession(Set<String> accountSet){
        if(CollectionUtils.isEmpty(accountSet)){
            return;
        }

        accountSet.forEach(account ->{
            Map<String, ? extends Session> map = sessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, account);
            if(map != null && map.size() > 0){
                for (String sessionId : map.keySet()) {
                    sessionRepository.deleteById(sessionId);
                }
            }
        });
    }

    /**
     * 重置用户权限
     * @param userId 用户id
     * @param authorities 权限列表
     */
    public void resetAuthorities(Long userId, List<GrantedAuthority> authorities){
        UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);
        Map<String, Session> redisSessionMap = sessionRepository.findByPrincipalName(String.valueOf(userId));
        redisSessionMap.values().forEach(session -> {
            SecurityContextImpl securityContext = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            securityContext.setAuthentication(newToken);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            sessionRepository.save(session);
        });
    }
}
