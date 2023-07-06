package com.wudgaby.starter.crypto.advice;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.starter.crypto.annotation.ApiEncryption;
import com.wudgaby.starter.crypto.util.AESUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @ClassName : EncryptionResponseBodyAdvice
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2023.07.04
 * @Desc :   接口数据加密返回
 */
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

        if(body == null){
            return null;
        }

        if(body instanceof ApiResult){
            ApiResult result = (ApiResult) body;
            if(ObjectUtil.isNotNull(result.getData())){
                String dataJson = JacksonUtil.serialize(result.getData());
                result.setData(AESUtil.encryptBase64(dataJson));
            }
            if(StrUtil.isNotBlank(result.getMessage())){
                result.setMessage(AESUtil.encryptBase64(result.getMessage()));
            }
            return body;
        }

        String dataJson = JacksonUtil.serialize(body);
        return ApiResult.success().data(AESUtil.encryptHex(dataJson));
    }
}
