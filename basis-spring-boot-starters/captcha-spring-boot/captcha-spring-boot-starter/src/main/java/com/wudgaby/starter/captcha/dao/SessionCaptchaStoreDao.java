package com.wudgaby.starter.captcha.dao;

import com.wudgaby.starter.captcha.core.dao.CaptchaStoreDao;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 14:36
 * @desc : session存储
 */
public class SessionCaptchaStoreDao implements CaptchaStoreDao {
    @Override
    public String save(String prefix, String data) {
        getSession().setAttribute(prefix, data);
        return getSession().getId();
    }

    @Override
    public String save(String prefix, String data, long second) {
        return save(prefix, data);
    }

    @Override
    public Optional<String> get(String prefix, String key) {
        return Optional.ofNullable(getSession().getAttribute(prefix)).map(v -> String.valueOf(v));
    }

    private HttpSession getSession(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

    @Override
    public void clear(String prefix, String key) {
        getSession().removeAttribute(prefix);
    }
}
