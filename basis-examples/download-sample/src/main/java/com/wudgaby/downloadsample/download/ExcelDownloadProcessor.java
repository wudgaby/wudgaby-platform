package com.wudgaby.downloadsample.download;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/6 0006 12:05
 * @desc :
 */
@Getter
public class ExcelDownloadProcessor<T> implements DownloadProcessor<T> {
    private HttpServletResponse response;
    private ExcelWriter writer;
    private WriteSheet sheet;
    private List<T> rowDataList;
    private String fileName;

    @SneakyThrows
    public ExcelDownloadProcessor(String fileName, HttpServletResponse response, Class<? extends T> clazz){
        this.fileName = fileName;
        this.response = response;
        this.rowDataList = new ArrayList<>(1);
        this.writer = EasyExcel.write(response.getOutputStream(), clazz).build();
        this.sheet = EasyExcel.writerSheet().build();
    }

    @Override
    public void process(T o) {
        this.rowDataList.add(o);
        this.writer.write(rowDataList,sheet);
        this.rowDataList.clear();
    }

    @Override
    public void finish() {
        DownloadUtil.streamDownloadForExcel(fileName, response, writer);
    }
}
