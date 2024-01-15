package com.wudgaby.platform.message.controller;

import com.aliyuncs.exceptions.ClientException;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.message.api.form.AliPushForm;
import com.wudgaby.platform.message.service.AliPushService;
import com.wudgaby.starter.resubmit.UnCheckRepeatSubmit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/11/18 11:45
 * @Desc :
 */
@Api(tags = "阿里推送")
@RestController
@AllArgsConstructor
public class AliPushController {
    private final AliPushService aliPushService;

    @ApiOperation("阿里高级推送")
    @PostMapping("/alipush/advPush")
    @UnCheckRepeatSubmit
    public ApiResult<Boolean> advancedPush(@Validated @RequestBody AliPushForm aliPushForm){
        aliPushService.advancedPush(aliPushForm);
        return ApiResult.success();
    }

    @ApiOperation("查询账号所绑定的设备")
    @GetMapping("/alipush/account")
    public ApiResult<List<String>> getAccount(@RequestParam String account) throws ClientException {
        return ApiResult.<List<String>>success().data(aliPushService.queryDeviceByAccount(account));
    }

    @ApiOperation("查询设备信息")
    @GetMapping("/alipush/device")
    public ApiResult<String> getDeviceInfo(@RequestParam String deviceId) throws ClientException {
        return ApiResult.<String>success().data(aliPushService.queryDeviceInfo(deviceId));
    }

    @ApiOperation("查询设备绑定的别名")
    @GetMapping("/alipush/aliase")
    public ApiResult<List<String>> getAliaseList(@RequestParam String deviceId) throws ClientException {
        return ApiResult.<List<String>>success().data(aliPushService.queryAliaseList(deviceId));
    }

    @ApiOperation(value = "降级测试")
    @GetMapping("/feignTest")
    public ApiResult<Integer> feignTest(@RequestParam Boolean flag){
        if(flag){
            return ApiResult.<Integer>success().data(100);
        }else{
            return ApiResult.<Integer>failure().message("失败了");
        }
    }
}
