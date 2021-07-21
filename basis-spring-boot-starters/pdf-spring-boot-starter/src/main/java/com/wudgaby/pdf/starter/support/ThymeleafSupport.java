package com.wudgaby.pdf.starter.support;

import lombok.AllArgsConstructor;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName : ThymeleafSupport
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/12 11:24
 * @Desc :
 */
@Component
@AllArgsConstructor
public class ThymeleafSupport {
    private final ITemplateEngine templateEngine;

    /**
     * 模板解析并输出到指定文件
     * @param contextMap
     * @param tmplName
     * @param file
     */
    public void write2File(Map<String, Object> contextMap, String tmplName, File outputFile) throws IOException {
        //构造上下文(Model)
        Context context = new Context();
        if(MapUtils.isNotEmpty(contextMap)){
            contextMap.forEach((k,v)-> context.setVariable(k, v));
        }

        //输出文件 渲染模板
        FileWriter write = new FileWriter(outputFile);
        templateEngine.process(tmplName, context, write);
    }

    /**
     * 模板解析并输出到内存
     * @param contextMap
     * @param tmplName
     */
    public String write2Content(Map<String, Object> contextMap, String tmplName){
        //构造上下文(Model)
        Context context = new Context();
        if(MapUtils.isNotEmpty(contextMap)){
            contextMap.forEach((k,v)-> context.setVariable(k, v));
        }

        //输出内存
        return templateEngine.process(tmplName, context);
    }
}
