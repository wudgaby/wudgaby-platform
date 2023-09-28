package com.wudgaby.starter.captcha.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wudgaby.starter.captcha.config.CaptchaProp;
import com.wudgaby.starter.captcha.core.CaptchaException;
import com.wudgaby.starter.captcha.core.CaptchaStoreDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 17:01
 * @desc :
 */
@Slf4j
@RequiredArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {
    private final CaptchaProp captchaProp;
    private final CaptchaStoreDao captchaStoreDao;

    /**
     * spring的路径匹配器
     */
    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(CollUtil.isEmpty(captchaProp.getAutoCheckMode().getFilterUrls())){
            filterChain.doFilter(request, response);
            return;
        }

        //url是否需要拦截
        boolean needValidate = false;
        String reqURI = request.getRequestURI();
        for(String filterUrl : captchaProp.getAutoCheckMode().getFilterUrls()){
            if(pathMatcher.match(filterUrl, reqURI)){
                needValidate = true;
                break;
            }
        }

        if(!needValidate){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            validate(request);
        } catch (CaptchaException captchaException) {
            log.error(captchaException.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("{\"code\":0,\"message\":\""+captchaException.getMessage()+"\"}");
            response.getWriter().flush();
            return;
        } catch (Exception ex){
            log.error(ex.getMessage(), ex);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("{\"code\":0,\"message\":\"系统异常\"}");
            response.getWriter().flush();
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 图片验证码校验
     *
     * @param request
     */
    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        String key = ServletRequestUtils.getStringParameter(request, captchaProp.getAutoCheckMode().getKeyName());
        String captchaInRequest = ServletRequestUtils.getStringParameter(request, captchaProp.getAutoCheckMode().getCaptchaName());

        if (StrUtil.isBlank(key) || StrUtil.isBlank(captchaInRequest)) {
            throw new CaptchaException("请填写验证码!");
        }
        String captchaInStore = captchaStoreDao.get(captchaProp.getStorePrefixKey(), key).orElse(null);
        if (StrUtil.isBlank(captchaInStore)) {
            throw new CaptchaException("验证码错误!");
        }
        if (!StrUtil.equalsIgnoreCase(captchaInRequest, captchaInStore)) {
            throw new CaptchaException("验证码错误!");
        }
        //验证通过 移除缓存
        captchaStoreDao.clear(captchaProp.getStorePrefixKey(), key);
    }
}
