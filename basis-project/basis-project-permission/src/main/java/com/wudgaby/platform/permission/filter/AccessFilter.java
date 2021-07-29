package com.wudgaby.platform.permission.filter;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.dto.OpenAuthority;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import com.wudgaby.platform.utils.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/18 23:17
 * @Desc :
 */
@Slf4j
public class AccessFilter implements Filter{
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    private Boolean dynamicAccess = true;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if(check(req.getRequestURI())){
            chain.doFilter(request, response);
        }
        response.getWriter().println(FastJsonUtil.collectToString(ApiResult.failure("认证不通过")));
    }

    /**
     * 是否放行
     * @param reqPath
     * @return
     */
    private boolean isPermit(String reqPath){
        return false;
    }

    private boolean check(String reqPath){
        if(!dynamicAccess){
            return true;
        }
        if(isPermit(reqPath)){
            return true;
        }

        return checkAuthorities(reqPath);
    }

    private boolean checkAuthorities(String reqPath){
        if (AuthorityConst.ADMIN.equals("")) {
            // 默认超级管理员账号,直接放行
            return true;
        }

        /*List<OpenAuthority> openAuthorityList = baseAuthorityService.findAuthorityByUser();
        baseAuthorityService.findAuthorityByRole()*/
        return true;
    }

}
