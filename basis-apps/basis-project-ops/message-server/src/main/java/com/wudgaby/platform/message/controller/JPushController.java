package com.wudgaby.platform.message.controller;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.message.api.form.JPushForm;
import com.wudgaby.platform.message.service.JPushService;
import com.wudgaby.plugin.resubmit.UnCheckRepeatSubmit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/11/18 11:45
 * @Desc :
 */
@Api(tags = "极光推送")
@RestController
@AllArgsConstructor
public class JPushController {
    private final JPushService jPushService;

    @ApiOperation("同步推送原生消息")
    @PostMapping("/jpush/sync")
    @UnCheckRepeatSubmit
    public ApiResult<Boolean> sendPush(@Validated @RequestBody JPushForm jPushForm){
        boolean isSuccess = jPushService.sendPush(jPushForm);
        return ApiResult.<Boolean> success().data(isSuccess);
    }

    @ApiOperation("异步推送原生消息")
    @PostMapping("/jpush/async")
    @UnCheckRepeatSubmit
    public ApiResult asyncSendPush(@Validated @RequestBody JPushForm jPushForm){
        jPushService.asyncSendPush(jPushForm);
        return ApiResult.success();
    }

    @ApiOperation("同步推送自定义消息")
    @PostMapping("/jpush/custom/sync")
    @UnCheckRepeatSubmit
    public ApiResult<Boolean> sendCustomPush(@Validated @RequestBody JPushForm jPushForm){
        boolean isSuccess = jPushService.sendCustomPush(jPushForm);
        return ApiResult.<Boolean> success().data(isSuccess);
    }

    @ApiOperation("异步推送自定义消息")
    @PostMapping("/jpush/custom/async")
    @UnCheckRepeatSubmit
    public ApiResult asyncSendCustomPush(@Validated @RequestBody JPushForm jPushForm){
        jPushService.asyncSendCustomPush(jPushForm);
        return ApiResult.success();
    }
}
