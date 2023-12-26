package com.wudgaby.starter.captcha.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import com.wudgaby.starter.captcha.core.CaptchaResultVO;
import com.wudgaby.starter.captcha.core.CaptchaStoreDao;
import com.wudgaby.starter.captcha.dao.SessionCaptchaStoreDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 10:09
 * @desc :
 */
@Api(tags = "验证码接口")
@RestController
@RequiredArgsConstructor
public class CaptchaEndpoint {
    private final CaptchaProp captchaProp;
    private final CaptchaStoreDao captchaStoreDao;

    /**
     * 生成验证码
     *
     * @param response 响应流
     */
    @ApiOperation(value = "生成验证码", notes = "根据Content-Type: application/json或image/gif生成不同类型数据", produces = MediaType.IMAGE_GIF_VALUE + "," + MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "${captcha.createPath:/captcha}")
    public void create(HttpServletRequest request, HttpServletResponse response) {
        createCaptcha(request, response);
    }

    @SneakyThrows
    private void createCaptcha(HttpServletRequest request, HttpServletResponse response){
        Captcha captcha = buildCaptcha();

        //生成结果
        String captchaResult = captcha.text();

        //存储
        String key = captchaStoreDao.save(captchaProp.getStorePrefixKey(), captchaResult, captchaProp.getDuration().getSeconds());

        //session存储模式支持json/image, 其他模式只支持json模式
        if(captchaStoreDao instanceof SessionCaptchaStoreDao){
            if(StrUtil.equalsIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)){
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                //不要base64的头部data:image/png;base64
                response.getWriter().println(JSONUtil.toJsonStr(new CaptchaResultVO().setKey(key).setCaptcha(captcha.toBase64())));
            }else{
                /*Cookie captchaKeyCookie = new Cookie(captchaProp.getAutoCheckMode().getKeyName(), "123");
                captchaKeyCookie.setMaxAge(600);
                captchaKeyCookie.setPath("/");
                response.addCookie(captchaKeyCookie);*/

                //设置响应头
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                response.setHeader("Pragma", "No-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
                captcha.out(response.getOutputStream());
            }
        }else{
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            //不要base64的头部data:image/png;base64
            response.getWriter().println(JSONUtil.toJsonStr(new CaptchaResultVO().setKey(key).setCaptcha(captcha.toBase64())));
        }
    }

    @SneakyThrows
    private Captcha buildCaptcha(){
        Captcha captcha;
        switch (captchaProp.getCaptchaType()){
            case SPEC:
                captcha = new SpecCaptcha(captchaProp.getWidth(), captchaProp.getHeight(), captchaProp.getLen());
                captcha.setFont(captchaProp.getFontType());
                captcha.setCharType(captchaProp.getCharType());
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(captchaProp.getWidth(), captchaProp.getHeight(), captchaProp.getLen());
                captcha.setFont(captchaProp.getFontType());
                break;
            case CHINESE_GIT:
                captcha = new ChineseGifCaptcha(captchaProp.getWidth(), captchaProp.getHeight(), captchaProp.getLen());
                captcha.setFont(captchaProp.getFontType());
                break;
            case ARITHMETIC:
                captcha = new ArithmeticCaptcha(captchaProp.getWidth(), captchaProp.getHeight(), captchaProp.getLen());
                captcha.setFont(captchaProp.getFontType());
                break;
            case GIF:
            default:
                //默认gif
                captcha = new GifCaptcha(captchaProp.getWidth(), captchaProp.getHeight(), captchaProp.getLen());
                captcha.setFont(captchaProp.getFontType());
                captcha.setCharType(captchaProp.getCharType());
                break;
        }
        return captcha;
    }
}
