package com.wudgaby.platform.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.google.common.net.MediaType;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/17 0017 14:16
 * @desc :
 */
@UtilityClass
public class WebServletUtil {
    @SuppressWarnings("all")
    public static void writeJson(HttpServletResponse response, Object object) {
        String content = JacksonUtil.serialize(object);
        response.setCharacterEncoding("UTF-8");
        ServletUtil.write(response, content, MediaType.JSON_UTF_8.toString());
    }

    public static void writeJson(HttpServletResponse response, Object object, int httpStatus) {
        response.setStatus(httpStatus);
        writeJson(response, object);
    }

    /**
     * 返回附件
     *
     * @param response 响应
     * @param filename 文件名
     * @param content  附件内容
     */
    @SuppressWarnings("all")
    public static void writeAttachment(HttpServletResponse response, String filename, byte[] content) throws IOException {
        // 设置 header 和 contentType
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType(MediaType.JSON_UTF_8.toString());
        // 输出附件
        IoUtil.write(response.getOutputStream(), false, content);
    }
}
