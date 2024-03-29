package com.wudgaby.platform.twofactorauth.sample.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.springext.RequestContextHolderSupport;
import com.wudgaby.platform.twofactorauth.sample.util.GoogleAuthenticator;
import com.wudgaby.platform.twofactorauth.sample.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.util.Map;

/**
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/12/9 23:07
 * @Desc :
 */
@Api(tags="2FA认证")
@RestController
public class LoginController {
    private Map<String, User> userStore = Maps.newHashMap();

    @ApiOperation("1注册")
    @PostMapping("register")
    @ApiOperationSupport(order = 1, author = "bird")
    public ApiResult register(@RequestBody User user){
        userStore.put(user.getAccount(), user);
        return ApiResult.success();
    }

    @ApiOperation("2登录")
    @PostMapping("/login")
    @ApiOperationSupport(order = 2, author = "bird")
    public ApiResult login(@RequestBody User user){
        User foundUser = userStore.get(user.getAccount());
        if(foundUser == null){
            return ApiResult.failure("账号错误");
        }
        if(!foundUser.getPassword().equals(user.getPassword())){
            return ApiResult.failure("密码错误");
        }

        if(StringUtils.isNotBlank(foundUser.getSecretKey())){
            if(!GoogleAuthenticator.checkCode(foundUser.getSecretKey(), user.getGoogleCode(), System.currentTimeMillis())){
                return ApiResult.failure("谷歌认证验证错误");
            }
        }

        RequestContextHolderSupport.getSession().setAttribute("user", foundUser);
        return ApiResult.success("登录成功");
    }

    @ApiOperation("3生成google密钥")
    @GetMapping("generateGoogleSecret")
    @ApiOperationSupport(order = 3, author = "bird")
    public ApiResult generateGoogleSecret(){
        User foundUser = (User)RequestContextHolderSupport.getSession().getAttribute("user");
        if(foundUser == null){
            return ApiResult.failure("请先登录");
        }

        //生成秘钥
        String secret = GoogleAuthenticator.getRandomSecretKey();
        //用户二维码内容
        String secretQrCode = GoogleAuthenticator.getGoogleAuthenticatorBarCode(secret, foundUser.getAccount(), "95至尊网");
        return ApiResult.success().data(ImmutableMap.of("secret",secret, "secretQrCode", secretQrCode));
    }

    @ApiOperation("4生成二维码")
    @GetMapping(value = "/genQrCode", produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperationSupport(order = 4, author = "bird")
    public void genQrCode(String secretQrCode) throws Exception{
        RequestContextHolderSupport.getResponse().setContentType("image/png");
        OutputStream stream = RequestContextHolderSupport.getResponse().getOutputStream();
        QrCodeUtil.generate(secretQrCode, 300, 300, ImgUtil.IMAGE_TYPE_JPG, stream);
        stream.flush();
    }

    @ApiOperation("5绑定google验证")
    @PostMapping("/bindGoogle")
    @ApiOperationSupport(order = 5, author = "bird")
    public ApiResult bindGoogle(String secret, Long googleCode) throws Exception{
        User foundUser = (User)RequestContextHolderSupport.getSession().getAttribute("user");
        if(foundUser == null){
            return ApiResult.failure("请先登录");
        }

        if(!GoogleAuthenticator.checkCode(secret, googleCode, System.currentTimeMillis())){
            return ApiResult.failure("谷歌双重认证错误");
        }

        foundUser.setSecretKey(secret);
        userStore.put(foundUser.getAccount(), foundUser);
        return ApiResult.success("绑定成功");
    }

    @ApiOperation("6登出")
    @DeleteMapping("/logout")
    @ApiOperationSupport(order = 6, author = "bird")
    public ApiResult logout() throws Exception{
        RequestContextHolderSupport.getSession().invalidate();
        return ApiResult.success("登出成功");
    }

    @ApiOperation("7谷歌双重认证登录")
    @PostMapping("/googleLogin")
    @ApiOperationSupport(order = 7, author = "bird")
    public ApiResult googleLogin(String account, Long googleCode) throws Exception{
        User foundUser = userStore.get(account);
        if(foundUser == null){
            return ApiResult.failure("账号错误");
        }

        if(!GoogleAuthenticator.checkCode(foundUser.getSecretKey(), googleCode, System.currentTimeMillis())){
            return ApiResult.failure("google双重认证错误");
        }

        RequestContextHolderSupport.getSession().setAttribute("user", foundUser);
        return ApiResult.success("google双重认证成功");
    }
}
