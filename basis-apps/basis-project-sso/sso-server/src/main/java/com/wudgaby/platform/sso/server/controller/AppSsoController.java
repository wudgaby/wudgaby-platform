package com.wudgaby.platform.sso.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.sso.server.entity.BaseApp;
import com.wudgaby.platform.sso.server.entity.User;
import com.wudgaby.platform.sso.server.form.LoginForm;
import com.wudgaby.platform.sso.server.service.BaseAppService;
import com.wudgaby.platform.sso.server.service.UserService;
import com.wudgaby.starter.redis.support.RedisSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/1 18:40
 * @Desc :   TODO
 */
@Api(tags = "app单点")
@AllArgsConstructor
@RestController
@RequestMapping("/app")
public class AppSsoController {
    private final UserService userService;
    private final BaseAppService baseAppService;
    private final SsoProperties ssoProperties;
    private final RedisSupport redisSupport;

    @ApiOperation(value = "登录", notes = "返回的code 通过 codeExToken接口换取token. 拿到的token 放到header中. 请求头key: X-AUTH-TOKEN")
    @PostMapping(SsoConst.SSO_LOGIN_URL)
    public ApiResult login(@RequestBody @Validated LoginForm loginForm){
        User user = userService.login(loginForm.getAccount(), loginForm.getPassword());

        SsoUserVo ssoUserVo = new SsoUserVo()
                .setUserId(String.valueOf(user.getId()))
                .setUsername(user.getUserName())
                .setAccount(user.getAccount())
                .setUserTel(user.getPhone())
                .setUserEmail(user.getEmail())
                .setUserPortrait(user.getAvatar())
                ;

        BaseApp baseApp = baseAppService.getOne(Wrappers.<BaseApp>lambdaQuery().eq(BaseApp::getAppCode, loginForm.getAppCode()));
        if(baseApp == null){
            return ApiResult.failure().message("未知应用");
        }
        if(baseApp.getStatus() == 0){
            return ApiResult.failure().message("该应用未开启");
        }

        //uuid + secretKey 5分钟
        String code = UUID.randomUUID().toString();
        //String sign = DigestUtils.sha1Hex(code + baseApp.getSecretKey());
        long expire = Duration.ofMinutes(5).getSeconds();
        //redisSupport.set(code, sign, expire);
        redisSupport.set(SsoConst.SSO_USER + code, ssoUserVo, expire);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        return ApiResult.success().data(resultMap);
    }

    @ApiOperation("code换取token")
    @GetMapping(SsoConst.SSO_TOKEN_URL)
    public ApiResult token(@RequestParam String code, @RequestParam String secret, @RequestParam String sign){
        //String redisSign = Optional.ofNullable(redisSupport.get(code)).map(String::valueOf).orElse(null);
        String reSign = DigestUtils.sha1Hex(code + secret);
        if(!StringUtils.equals(reSign, sign)){
            return ApiResult.failure().message("签名错误.");
        }

        SsoUserVo ssoUserVo = (SsoUserVo)redisSupport.get(SsoConst.SSO_USER + code);
        if(ssoUserVo == null) {
            return ApiResult.failure().message("已过期");
        }
        String token = UUID.randomUUID().toString();
        redisSupport.set(token, ssoUserVo, Duration.ofDays(7).getSeconds());
        return ApiResult.success().data(token);
    }

    @ApiOperation("签名测试")
    @GetMapping("/sign")
    @ApiIgnore
    public ApiResult token(@RequestParam String code, @RequestParam String secret){
        String sign = DigestUtils.sha1Hex(code + secret);
        return ApiResult.success().data(sign);
    }

    @ApiOperation("检查状态")
    @GetMapping(SsoConst.SSO_CHECK_URL)
    public ApiResult check(@RequestParam String token){
        SsoUserVo ssoUserVo = (SsoUserVo)redisSupport.get(token);
        if(ssoUserVo == null) {
            return ApiResult.failure();
        }
        return ApiResult.success().data(ssoUserVo);
    }

    @ApiOperation("登出")
    @DeleteMapping(SsoConst.SSO_LOGOUT_URL)
    public ApiResult logout(@RequestParam String token){
        redisSupport.del(token);
        return ApiResult.success();
    }
}
