package com.wudgaby.pdf.starter.support;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.wudgaby.pdf.starter.manager.CustomFontManager;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * @ClassName : PdfSupport
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/12 11:41
 * @Desc :   TODO
 */
public class PdfSupport {
    private static final String DEFAULT_FONT = "jdzsj";

    private PdfSupport(){}

    /**
     * 输出pdf到文件
     * @param content
     * @param outputPdf
     * @param fontName
     * @throws IOException
     */
    public static void convert2Pdf(@Nonnull String content, @Nonnull File outputPdf, String fontName) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        setFont(fontName, converterProperties);
        HtmlConverter.convertToPdf(content, new FileOutputStream(outputPdf), converterProperties);
    }

    /**
     * 输出pdf到文件
     * @param content
     * @param outputPdf
     * @param fontName
     * @throws IOException
     */
    public static void convert2Pdf(@Nonnull File inputFile, @Nonnull File outputPdf, String fontName) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        setFont(fontName, converterProperties);
        HtmlConverter.convertToPdf(new FileInputStream(inputFile), new FileOutputStream(outputPdf), converterProperties);
    }

    /**
     * pdfHTML 设置字体
     * @param fontName
     * @param converterProperties
     * @throws IOException
     */
    private static void setFont(String fontName, ConverterProperties converterProperties) throws IOException {
        FontProgram fontProgram = FontProgramFactory.createFont(CustomFontManager.getFontPath(Optional.ofNullable(fontName).orElse(DEFAULT_FONT)));
        FontSet fontSet = new FontSet();
        fontSet.addFont(fontProgram, PdfEncodings.IDENTITY_H);
        FontProvider fontProvider = new FontProvider(fontSet);
        converterProperties.setFontProvider(fontProvider);
    }
}
