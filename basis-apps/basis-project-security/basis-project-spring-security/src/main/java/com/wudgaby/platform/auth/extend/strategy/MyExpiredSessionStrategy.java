package com.wudgaby.platform.auth.extend.strategy;

import com.google.common.base.Charsets;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.JacksonUtil;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/3 18:42
 * @Desc :
 */
public class MyExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletResponse response = event.getResponse();
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = JacksonUtil.serialize(ApiResult.<String>failure().message("用户已在其他地方登录..."));
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
