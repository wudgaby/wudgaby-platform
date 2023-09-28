package com.wudgaby.sample.demo;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.spring.annotation.Captcha;
import cloud.tianai.captcha.spring.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.plugins.secondary.SecondaryVerificationApplication;
import cloud.tianai.captcha.spring.request.CaptchaRequest;
import cloud.tianai.captcha.spring.vo.CaptchaResponse;
import cloud.tianai.captcha.spring.vo.ImageCaptchaVO;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/demo")
public class SlideCaptchaController {
    @Autowired
    private ImageCaptchaApplication application;
    @Autowired
    private ImageCaptchaApplication sca;

    @GetMapping("/generate")
    public String generate(){
        // 1.生成滑块验证码(该数据返回给前端用于展示验证码数据)
        CaptchaResponse<ImageCaptchaVO> res1 = application.generateCaptcha(CaptchaTypeConstant.SLIDER);
        log.info("{}", res1);
        return JSONUtil.toJsonStr(res1);
    }

    @GetMapping("/generate2")
    public String generate2(){
        // 1.生成滑块验证码(该数据返回给前端用于展示验证码数据)
        CaptchaResponse<ImageCaptchaVO> res1 = sca.generateCaptcha(CaptchaTypeConstant.SLIDER);
        log.info("{}", res1);
        return JSONUtil.toJsonStr(res1);
    }

    @Captcha("SLIDER")
    @GetMapping("/login")
    public String login(@RequestBody CaptchaRequest<Map> request) {
        // 进入这个方法就说明已经校验成功了
        log.info("{}", request);
        return "success";
    }

    @GetMapping("/check")
    public String check(String id){
        // 2.前端滑动完成后把数据传入后端进行校验是否通过，
        // 	参数1: 生成的验证码对应的id, 由前端传过来
        // 	参数2: 滑动轨迹验证码相关数据 ImageCaptchaTrack， 由前端传过来
        // 返回 match.isSuccess() 如果为true， 则验证通过
        ImageCaptchaTrack sliderCaptchaTrack = new ImageCaptchaTrack();
        ApiResponse<?> match = application.matching(id, sliderCaptchaTrack);
        return JSONUtil.toJsonStr(match);
    }

    @GetMapping("/check2")
    @ResponseBody
    public boolean check2Captcha(@RequestParam("id") String id) {
        // 如果开启了二次验证
        if (sca instanceof SecondaryVerificationApplication) {
            return ((SecondaryVerificationApplication) sca).secondaryVerification(id);
        }
        return false;
    }
}
