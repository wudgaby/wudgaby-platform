package com.wudgaby.platform.twofactorauth.sample.controller;

import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.twofactorauth.sample.util.QRCodeUtil;
import com.wudgaby.platform.twofactorauth.sample.vo.User;
import com.wudgaby.platform.twofactorauth.sample.util.GoogleAuthenticator;
import com.wudgaby.platform.webcore.support.RequestContextHolderSupport;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;
import java.util.Map;

/**
 * @ClassName : LoginController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/9 23:07
 * @Desc :   TODO
 */
@RestController
public class LoginController {
    private Map<String, User> userStore = Maps.newHashMap();

    @ApiOperation("1注册")
    @PostMapping("register")
    public ApiResult register(@RequestBody User user){
        userStore.put(user.getAccount(), user);
        return ApiResult.success();
    }

    @ApiOperation("2登录")
    @PostMapping("/login")
    public ApiResult login(@RequestBody User user){
        User foundUser = userStore.get(user.getAccount());
        if(foundUser == null){
            return ApiResult.failure("账号错误");
        }
        if(!foundUser.getPassword().equals(user.getPassword())){
            return ApiResult.failure("密码错误");
        }

        if(StringUtils.isNotBlank(foundUser.getSecretKey())){
            if(!GoogleAuthenticator.check_code(foundUser.getSecretKey(), user.getGoogleCode(), System.currentTimeMillis())){
                return ApiResult.failure("谷歌认证验证错误");
            }
        }

        RequestContextHolderSupport.getSession().setAttribute("user", foundUser);
        return ApiResult.success("登录成功");
    }

    @ApiOperation("3生成google密钥")
    @GetMapping("generateGoogleSecret")
    public ApiResult generateGoogleSecret(){
        User foundUser = (User)RequestContextHolderSupport.getSession().getAttribute("user");
        if(foundUser == null){
            return ApiResult.failure("请先登录");
        }

        String secretKey = GoogleAuthenticator.getRandomSecretKey();
        //用户二维码内容
        String secretQrCode = GoogleAuthenticator.getGoogleAuthenticatorBarCode(secretKey, foundUser.getAccount(), "www.wudgaby.com");
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("secret", secretKey);
        resultMap.put("secretQrCode", secretQrCode);
        return ApiResult.success().data(resultMap);
    }

    @ApiOperation("4生成二维码")
    @GetMapping(value = "/genQrCode", produces = MediaType.IMAGE_PNG_VALUE)
    public void genQrCode(String secretQrCode) throws Exception{
        RequestContextHolderSupport.getResponse().setContentType("image/png");
        OutputStream stream = RequestContextHolderSupport.getResponse().getOutputStream();
        QRCodeUtil.encode(secretQrCode,stream);
    }

    @ApiOperation("5绑定google验证")
    @PostMapping("/bindGoogle")
    public ApiResult bindGoogle(String secret, Long googleCode) throws Exception{
        User foundUser = (User)RequestContextHolderSupport.getSession().getAttribute("user");
        if(foundUser == null){
            return ApiResult.failure("请先登录");
        }

        if(!GoogleAuthenticator.check_code(secret, googleCode, System.currentTimeMillis())){
            return ApiResult.failure("谷歌认证验证错误");
        }

        foundUser.setSecretKey(secret);
        userStore.put(foundUser.getAccount(), foundUser);
        return ApiResult.success("绑定成功");
    }

    @ApiOperation("6登出")
    @DeleteMapping("/logout")
    public ApiResult logout() throws Exception{
        RequestContextHolderSupport.getSession().invalidate();
        return ApiResult.success("登出成功");
    }

    @ApiOperation("7谷歌登录")
    @PostMapping("/googleLogin")
    public ApiResult googleLogin(String account, Long googleCode) throws Exception{
        User foundUser = userStore.get(account);
        if(foundUser == null){
            return ApiResult.failure("账号错误");
        }

        if(!GoogleAuthenticator.check_code(foundUser.getSecretKey(), googleCode, System.currentTimeMillis())){
            return ApiResult.failure("谷歌认证验证错误");
        }

        RequestContextHolderSupport.getSession().setAttribute("user", foundUser);
        return ApiResult.success("谷歌登录成功");
    }

}
