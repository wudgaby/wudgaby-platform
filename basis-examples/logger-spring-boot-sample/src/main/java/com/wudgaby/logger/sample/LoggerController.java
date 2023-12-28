package com.wudgaby.logger.sample;

import com.wudgaby.logger.api.annotation.AccessLogger;
import com.wudgaby.platform.core.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/4 15:42
 * @Desc :
 */
@Api(value = "日志演示", tags = "日志")
@RestController
public class LoggerController {

    @AccessLogger(ignore = true)
    @ApiOperation(value = "忽略", notes = "试试")
    @GetMapping("/loggerDemo0")
    public ApiResult loggerDemo0(){
        return ApiResult.success().message("你好!!");
    }

    @AccessLogger(value = "无参数")
    @ApiOperation(value = "无参数", notes = "试试")
    @GetMapping("/loggerDemo1")
    public ApiResult loggerDemo1(){
        return ApiResult.success().message("你好!!");
    }

    @AccessLogger(value = "有参数")
    @ApiOperation(value = "有参数", notes = "试试")
    @GetMapping("/loggerDemo2")
    public ApiResult loggerDemo2(@RequestParam String name){
        return ApiResult.success().message("你好:" + name);
    }

    @AccessLogger(value = "包含无法序列化参数")
    @ApiOperation(value = "包含无法序列化参数", notes = "试试2")
    @GetMapping("/loggerDemo3")
    public ApiResult loggerDemo3(@RequestParam String name, HttpServletRequest request){
        return ApiResult.success().message("你好:" + name);
    }

    @SneakyThrows
    @ApiOperation(value = "download")
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(HttpServletResponse response){
        response.getOutputStream().println("success");
    }

    @SneakyThrows
    @ApiOperation(value = "返回无法序列化参数")
    @GetMapping(value = "/resp", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public HttpServletResponse resp(HttpServletResponse response){
        return response;
    }

}
