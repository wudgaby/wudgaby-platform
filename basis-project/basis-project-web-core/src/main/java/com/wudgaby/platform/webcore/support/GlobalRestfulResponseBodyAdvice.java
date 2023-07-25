package com.wudgaby.platform.webcore.support;

import com.wudgaby.platform.core.annotation.ApiResultView;
import com.wudgaby.platform.core.annotation.OriginalResultView;
import com.wudgaby.platform.core.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @ClassName : GlobalRestfulResponseBodyAdvice
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/26/026 22:21
 * @Desc :   返回值统一处理, 不推荐: swagger文档识别不出返回结果格式
 *          参考使用 FastJsonViewResponseBodyAdvice, JSONPResponseBodyAdvice
 */
@Slf4j
@Deprecated
//@ControllerAdvice
public class GlobalRestfulResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //每个方法加@ApiResultView 有点多余

        /*AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ApiResultView.class);
        boolean hasApiResultView = returnType.getContainingClass().isAnnotationPresent(ApiResultView.class)
                                        || returnType.hasMethodAnnotation(ApiResultView.class);
        boolean hasOriginalResultView = returnType.hasMethodAnnotation(OriginalResultView.class);
        return hasApiResultView && !hasOriginalResultView;*/

        return !returnType.hasMethodAnnotation(OriginalResultView.class);
    }

    /**
     * 非注解(originalReturnValue)标记的 都返回 ApiResult格式
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //body = Object时 使用MappingJackson2HttpMessageConverter
        //body = string时 使用StringHttpMessageConverter

        if(body == null){
            return body;
        }

        if(body instanceof ApiResult){
            //可以加些料
           return body;
        }
        return ApiResult.success(body);
    }
}
