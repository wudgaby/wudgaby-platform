package com.wudgaby.downloadsample.download;

import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/6 0006 12:05
 * @desc :
 */
public class CommonDownloadProcessor<T> implements DownloadProcessor<T> {
    private HttpServletResponse response;
    private List<T> rowDataList;
    private String fileName;

    public CommonDownloadProcessor(String fileName, HttpServletResponse response){
        this.fileName = fileName;
        this.response = response;
        this.rowDataList = new ArrayList<>(1);
        DownloadUtil.processHttpResponse(fileName, response);
    }

    @Override
    @SneakyThrows
    public void process(T o) {
        response.getOutputStream().write(o.toString().getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().write("\n".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void finish() {
        DownloadUtil.streamDownload(fileName, response);
    }
}
