package com.wudgaby.starter.data.security.crypt.advice;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.data.security.crypt.DataCryptUtil;
import com.wudgaby.starter.data.security.crypt.annotation.ApiEncryption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2023.07.04
 * @Desc :   接口数据加密返回
 */
@Slf4j
@RestControllerAdvice
public class EncryptionResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(ApiEncryption.class);
    }

    /**
     * 非注解(originalReturnValue)标记的 都返回 ApiResult格式
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //body = Object时 使用MappingJackson2HttpMessageConverter
        //body = string时 使用StringHttpMessageConverter
        log.info("EncryptionResponseBodyAdvice处理");

        if(body == null){
            return null;
        }

        if(body instanceof ApiResult){
            ApiResult result = (ApiResult) body;
            if(ObjectUtil.isNotNull(result.getData())){
                result.setData(DataCryptUtil.encrypt(result.getData()));
            }
            if(StrUtil.isNotBlank(result.getMessage())){
                result.setMessage(DataCryptUtil.encrypt(result.getMessage()));
            }
            return body;
        }

        return ApiResult.success().data(DataCryptUtil.encrypt(body));
    }
}
