package com.wudgaby.platform.message.controller;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.message.api.form.AlarmForm;
import com.wudgaby.platform.message.service.AlarmService;
import com.wudgaby.plugin.resubmit.UnCheckRepeatSubmit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : MailController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 10:44
 * @Desc :
 */
@Slf4j
@Api(tags = "告警功能")
@RestController
@AllArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @ApiOperation("告警")
    @PostMapping("/alarm")
    @UnCheckRepeatSubmit
    public ApiResult sendAlarm(@Validated @RequestBody AlarmForm alarmForm) {
        alarmService.sendAlarmInfo(alarmForm);
        return ApiResult.success();
    }
}
