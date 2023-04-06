package com.wudgaby.downloadsample.download;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.wudgaby.platform.core.result.ApiResult;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/6 0006 11:36
 * @desc :
 */
@Slf4j
@UtilityClass
public class DownloadUtil {
    /**
     * easyexcel流式下载
     * @param fileName
     * @param response
     * @param writer
     */
    @SneakyThrows
    public static void streamDownloadForExcel(String fileName, HttpServletResponse response, ExcelWriter writer){
        processHttpResponse(fileName, response);
        try(ServletOutputStream out = response.getOutputStream()){
            writer.finish();
            out.flush();
        }catch (Exception e){
            log.error("下载文件失败!", e);
            // 重置response
            //response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JSON.toJSONString(ApiResult.failure().message("下载文件失败!")));
        }
    }

    @SneakyThrows
    public static void streamDownload(String fileName, HttpServletResponse response){
        processHttpResponse(fileName, response);
        try(ServletOutputStream out = response.getOutputStream()){
            out.flush();
        }catch (Exception e){
            log.error("下载文件失败!", e);
            // 重置response
            //response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JSON.toJSONString(ApiResult.failure().message("下载文件失败!")));
        }
    }



    /**
     * 下载文件
     * @param fileName
     * @param file
     * @param response
     */
    @SneakyThrows
    public static void downloadFile(String fileName, File file, HttpServletResponse response){
        processHttpResponse(fileName, response);
        try(InputStream fis = new BufferedInputStream(new FileInputStream(file));
            OutputStream bos = new BufferedOutputStream(response.getOutputStream())){
            IOUtils.copy(fis, bos);
            bos.flush();
        }catch (Exception e){
            log.error("下载文件失败!", e);
            // 重置response
            //response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JSON.toJSONString(ApiResult.failure().message("下载文件失败!")));
        }
    }

    @SneakyThrows
    public static void downloadExcel(String fileName, HttpServletResponse response, List<T> records){
        processHttpResponse(fileName, response);
        try {
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), T.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.write(records, writeSheet);
            excelWriter.finish();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 重置response
            //response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JSON.toJSONString(ApiResult.failure().message("生成数据失败")));
        }
    }

    /**
     * 设置下载响应头
     * @param fileName
     * @param response
     */
    @SneakyThrows
    public static void processHttpResponse(String fileName, HttpServletResponse response){
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    }
}
