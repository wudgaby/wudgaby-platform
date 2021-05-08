package com.wudgaby.platform.webcore.rest;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.result.enums.SystemResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Map;

/**
 * @ClassName : NotFoundController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/4 16:29
 * @Desc :   TODO
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestController
@ApiIgnore
public class NotFoundController implements ErrorController {
    private static final String ERROR_PATH = "/error";
    private final ErrorAttributes errorAttributes;

    public NotFoundController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @ApiIgnore
    @RequestMapping(value = ERROR_PATH)
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult handleError(HttpServletRequest request) {
        ServletWebRequest requestAttributes =  new ServletWebRequest(request);
        Map<String, Object> attr = this.errorAttributes.getErrorAttributes(requestAttributes, false);

        String message = MessageFormat.format("[{0}] {1} 未找到该接口!", request.getMethod(), attr.get("path"));
        log.error("{}", message);

        return ApiResult.failure(SystemResultCode.NOT_FOUND).message(message);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
