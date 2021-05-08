package com.wudgaby.pdf.starter;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.wudgaby.pdf.starter.manager.CustomFontManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;

/**
 * @ClassName : ThymeleafDemo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/6 18:10
 * @Desc :   TODO
 */
@Component
public class ThymeleafDemo {
    @Autowired
    private ITemplateEngine templateEngine;
    public void demo() throws IOException {
        //构造上下文(Model)
        Context context = new Context();
        context.setVariable("name", "蔬菜列表");
        context.setVariable("array", new String[]{"土豆", "番茄", "白菜", "芹菜", "abc"});

        //输出文件 渲染模板
        FileWriter write = new FileWriter("result.example");
        templateEngine.process("example", context, write);


        String htmlContent = templateEngine.process("example", context);

        // IO
        File htmlSource = new File("result.example");
        File pdfDest = new File("output.pdf");

        // pdfHTML 设置字体
        ConverterProperties converterProperties = new ConverterProperties();
        FontProgram fontProgram = FontProgramFactory.createFont(CustomFontManager.getFontPath("zjlst"));
        FontSet fontSet = new FontSet();
        fontSet.addFont(fontProgram, PdfEncodings.IDENTITY_H);
        FontProvider fontProvider = new FontProvider(fontSet);
        converterProperties.setFontProvider(fontProvider);

        //HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
        HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(pdfDest), converterProperties);
    }
}
