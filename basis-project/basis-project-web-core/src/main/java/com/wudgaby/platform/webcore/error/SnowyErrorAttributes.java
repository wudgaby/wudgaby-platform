package com.wudgaby.platform.webcore.error;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpStatus;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.result.enums.SystemResultCode;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 将系统管理未知错误异常，输出格式重写为我们熟悉的响应格式
 *
 * @author yubaoshan
 * @date 2020/4/14 22:21
 */
public class SnowyErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions attributeOptions) {

        // 1.先获取spring默认的返回内容
        Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, attributeOptions);

        // 2.如果返回的异常是ServiceException，则按ServiceException响应的内容进行返回
        Throwable throwable = this.getError(webRequest);
        if (throwable instanceof BusinessException) {
            BusinessException businessException = (BusinessException) throwable;
            return BeanUtil.beanToMap(ApiResult.failure().code(businessException.getErrorCode()).message(businessException.getMessage()));
        }

        // 3.如果返回的是404 http状态码
        Integer status = (Integer) defaultErrorAttributes.get("status");
        if (status.equals(HttpStatus.HTTP_NOT_FOUND)) {
            return BeanUtil.beanToMap(ApiResult.failure(SystemResultCode.NOT_FOUND));
        }

        // 4.无法确定的返回服务器异常
        return BeanUtil.beanToMap(ApiResult.failure(SystemResultCode.SYSTEM_INNER_ERROR));
    }

}