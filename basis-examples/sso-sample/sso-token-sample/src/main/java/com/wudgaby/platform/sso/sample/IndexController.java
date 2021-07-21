package com.wudgaby.platform.sso.sample;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName : IndexController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/24 16:19
 * @Desc :
 */
@RestController
public class IndexController {
    @GetMapping("/")
    public ApiResult index(HttpServletRequest request){
        return ApiResult.success().data(request.getSession().getAttribute(SsoConst.SSO_USER));
    }
}
