package com.wudgaby.platform.message.service;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.message.api.enmus.SmsTmplEnum;
import com.wudgaby.platform.message.api.form.SmsTmplForm;
import com.wudgaby.platform.message.config.properties.SmsProperties;
import com.wudgaby.platform.message.constants.SystemConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @ClassName : SmsService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 9:59
 * @Desc :   短信服务
 */
@Slf4j
@Service
@AllArgsConstructor
public class SmsService {
    /**
     * 短信成功码
     */
    private static final int SMS_SUCCESS_CODE = 0;

    private final SmsProperties smsProperties;
    private final SmsSingleSender smsSingleSender;
    private final SmsMultiSender smsMultiSender;


    /**
     * 单发自定义消息
     * 不支持自定义消息
     *
     * @param phoneNum
     * @param message
     */
    @Deprecated
    public ApiResult sendSingleSms(String phoneNum, String message) {
        try {
            SmsSingleSenderResult result = smsSingleSender.send(SystemConstants.SMS_NORMAL_TYPE, SystemConstants.SMS_CHINA_CODE, phoneNum, message, StringUtils.EMPTY, StringUtils.EMPTY);
            log.info("[短信发送] 结果 {}", result.toString());
            if (SMS_SUCCESS_CODE == result.result) {
                return ApiResult.success().message("短信发送成功");
            }
            return ApiResult.failure().code(result.result).message(result.errMsg);
        } catch (Exception e) {
            log.error("[短信发送] 异常", e);
            throw new BusinessException(ApiResult.failure().getCode(), "短信发送异常.");
        }

    }

    /**
     * 模板单发
     *
     * @param phoneNum
     * @param smsTmplEnum
     * @param params
     */
    public ApiResult sendSingleSms(String phoneNum, SmsTmplEnum smsTmplEnum, String[] params) {
        log.info("短信模板参数: {}", Arrays.toString(params));
        try {
            SmsSingleSenderResult result = smsSingleSender.sendWithParam(SystemConstants.SMS_CHINA_CODE, phoneNum, smsTmplEnum.getCode(),
                    params, smsProperties.getSign(), StringUtils.EMPTY, StringUtils.EMPTY);
            log.info("[短信发送] 结果 {}", result.toString());
            if (SMS_SUCCESS_CODE == result.result) {
                return ApiResult.success().message("短信发送成功");
            }
            return ApiResult.failure().code(result.result).message(result.errMsg);
        } catch (Exception e) {
            log.error("[短信发送] 异常", e);
            throw new BusinessException(ApiResult.failure().getCode(), "短信发送异常.");
        }
    }

    /**
     * 群发自定义消息
     * 不支持自定义消息
     *
     * @param phoneNums
     * @param message
     */
    @Deprecated
    public ApiResult sendMultiSms(String[] phoneNums, String message) {
        try {
            SmsMultiSenderResult result = smsMultiSender.send(SystemConstants.SMS_NORMAL_TYPE, SystemConstants.SMS_CHINA_CODE, phoneNums, message, StringUtils.EMPTY, StringUtils.EMPTY);
            log.info("[短信发送] 结果 {}", result.toString());
            if (SMS_SUCCESS_CODE == result.result) {
                return ApiResult.success().message("短信发送成功");
            }
            return ApiResult.failure().code(result.result).message(result.errMsg);
        } catch (Exception e) {
            log.error("[短信发送] 异常", e);
            throw new BusinessException(ApiResult.failure().getCode(), "短信发送异常.");
        }
    }

    /**
     * 模板群发
     * 当多个手机号中 出现错误手机号时, 正确手机号能发送成功.
     * 返回多个手机号发送结果.
     *
     * @param smsTmplForm
     */
    public ApiResult sendTmplSms(SmsTmplForm smsTmplForm) {
        log.info("短信模板参数: {}", Arrays.toString(smsTmplForm.getParams()));
        try {
            SmsMultiSenderResult result = smsMultiSender.sendWithParam(SystemConstants.SMS_CHINA_CODE, smsTmplForm.getPhoneNums(), smsTmplForm.getSmsTmpl().getCode(),
                    smsTmplForm.getParams(), smsProperties.getSign(), StringUtils.EMPTY, StringUtils.EMPTY);
            log.info("[短信发送] 结果 {}", result.toString());
            if (SMS_SUCCESS_CODE == result.result) {
                return ApiResult.success().message("短信发送成功");
            }
            return ApiResult.failure().code(result.result).message(result.errMsg);
        } catch (Exception e) {
            log.error("[短信发送] 异常", e);
            throw new BusinessException(ApiResult.failure().getCode(), "短信发送异常.");
        }
    }
}
