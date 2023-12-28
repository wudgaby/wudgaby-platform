package com.wudgaby.codegen.ui.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.google.common.base.Charsets;
import com.wudgaby.codegen.ui.entity.TableEntity;
import com.wudgaby.codegen.ui.form.CodeDownLoadForm;
import com.wudgaby.codegen.ui.form.TableQueryForm;
import com.wudgaby.codegen.ui.service.GeneratorCodeService;
import com.wudgaby.codegen.ui.service.TableService;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.vo.PageVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/6/006 23:35
 * @Desc :
 */
@Slf4j
@ApiIgnore
@RestController
@AllArgsConstructor
@RequestMapping(GeneratorCodeController.ROOT_PATH)
public class GeneratorCodeController {
    public static final String ROOT_PATH = "/codeGen";

    private final GeneratorCodeService generatorCodeService;
    private final TableService tableService;

    @GetMapping("/tables")
    public ApiPageResult<List<TableEntity>> showTables(TableQueryForm tableQueryForm){
        PageVo pageVo = tableService.getTableList(tableQueryForm);
        return ApiPageResult.success(pageVo);
    }

    @GetMapping("/genCode")
    public void generatorCode(CodeDownLoadForm codeDownLoadForm, HttpServletResponse response) throws IOException {
        String fileName = "代码生成-" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN) + ".rar";
        String downloadFileName = URLEncoder.encode(fileName, "UTF-8");

        OutputStream outputStream = response.getOutputStream();
        try{
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName + "\"");
            response.setCharacterEncoding(Charsets.UTF_8.name());
            generatorCodeService.downloadZip(outputStream, codeDownLoadForm);
        }catch (Exception ex) {
            response.setContentType("application/json; charset=UTF-8");
            response.setHeader("Content-Disposition", "inline");
            response.setCharacterEncoding(Charsets.UTF_8.name());
            outputStream.write(new String("代码生成失败! " + ex.getMessage()).getBytes(StandardCharsets.UTF_8));
        } finally {
            response.flushBuffer();
        }
    }
}
