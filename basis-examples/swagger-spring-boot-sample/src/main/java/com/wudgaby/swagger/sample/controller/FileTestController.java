package com.wudgaby.swagger.sample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/5/22 10:58
 * @Desc :
 */
@Api(tags = "上传测试")
@Slf4j
@Controller
public class FileTestController {
    @ApiOperation("上传")
    @PostMapping("/fileUpload")
    @ResponseBody
    public String fileUpload(@Validated FileVo fileVo){
        log.info("{}", fileVo);
        return "success";
    }

    @Data
    public static class FileVo{
        private String name;
        @Valid
        private DataVo serviceDesc;
    }

    @Data
    private static class DataVo{
        @Valid
        private FileDataVo data;
    }

    @Data
    private static class FileDataVo{
        @NotNull(message="file is null")
        private MultipartFile file;
    }
}
