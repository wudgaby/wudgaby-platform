package com.wudgaby.starter.captcha.config;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import com.wudgaby.starter.captcha.core.dao.CaptchaStoreDao;
import com.wudgaby.starter.captcha.dao.SessionCaptchaStoreDao;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 10:09
 * @desc :
 */
@Controller
@RequiredArgsConstructor
public class CaptchaEndpoint {
    private final CaptchaProp captchaProp;
    private final CaptchaStoreDao captchaStoreDao;

    /**
     * 生成验证码
     *
     * @param response 响应流
     */
    @GetMapping("${captcha.createPath:/captcha}")
    public void create(HttpServletResponse response) {
        createCaptcha(response);
    }

    @SneakyThrows
    private void createCaptcha(HttpServletResponse response){
        Captcha captcha = buildCaptcha();

        //生成结果
        String captchaResult = captcha.text();

        //存储
        String key = captchaStoreDao.save(captchaProp.getCaptchaStorePrefixKey(), captchaResult, captchaProp.getDuration().getSeconds());

        if(captchaStoreDao instanceof SessionCaptchaStoreDao){
            //设置响应头
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            captcha.out(response.getOutputStream());
        }else{
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            //不要base64的头部data:image/png;base64
            Map<String, String> resultMap = Maps.newHashMap();
            resultMap.put("key", key);
            resultMap.put("captchaImg", captcha.toBase64(""));
            response.getWriter().println(JSONUtil.toJsonStr(resultMap));
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
                captcha = new GifCaptcha(captchaProp.getWidth(), captchaProp.getHeight(), captchaProp.getLen());
                captcha.setFont(captchaProp.getFontType());
                captcha.setCharType(captchaProp.getCharType());
                break;
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
