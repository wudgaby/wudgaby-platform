package com.wudgaby.platform.websocket.handler;

import com.wudgaby.platform.websocket.consts.SysConsts;
import com.wudgaby.platform.websocket.vo.FastPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName : SpringHandshakeInterceptor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/26 14:27
 * @Desc :   TODO
 */
@Slf4j
@Component
public class SpringHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("开始握手");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
            FastPrincipal fastPrincipal = (FastPrincipal)httpServletRequest.getSession().getAttribute(SysConsts.SESSION_USER);

            //Map<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), StandardCharsets.UTF_8);

            /*String token = Optional.ofNullable(ServletUtil.getCookie(httpServletRequest, SysConsts.TOKEN)).map(Cookie::getValue).orElse("");
            if(StringUtils.isBlank(token)){
                token = httpServletRequest.getHeader(SysConsts.X_AUTH_TOKEN);
            }
            if(StringUtils.isBlank(token)){
                token = httpServletRequest.getParameter(SysConsts.TOKEN);
            }
            if(StringUtils.isBlank(token)){
                log.error("无效token");
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return false;
            }*/

            if(fastPrincipal == null){
                log.error("未登录的用户");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            attributes.put(SysConsts.TOKEN, fastPrincipal);
            log.info("用户 token {} 握手成功！", fastPrincipal);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("握手完成");
    }
}
