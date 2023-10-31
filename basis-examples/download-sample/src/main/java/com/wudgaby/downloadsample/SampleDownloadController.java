package com.wudgaby.downloadsample;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudgaby.downloadsample.download.AbstractResultHandler;
import com.wudgaby.downloadsample.download.CommonDownloadProcessor;
import com.wudgaby.downloadsample.download.DownloadProcessor;
import com.wudgaby.downloadsample.download.DownloadUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/6 0006 17:38
 * @desc :
 */
@Slf4j
@RestController
@AllArgsConstructor
public class SampleDownloadController {
    private final PeopleMapper peopleMapper;

    @ApiOperation(value = "streamDownload")
    @GetMapping(value = "/streamDownload", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void streamDownload(HttpServletResponse response){
        //DownloadProcessor downloadProcessor = new ExcelDownloadProcessor<>("测试1.xls", response, People.class);
        DownloadProcessor downloadProcessor = new CommonDownloadProcessor("测试3.txt", response);
        AbstractResultHandler resultHandler = new AbstractResultHandler<People, People>(downloadProcessor) {
            @Override
            public People processing(People o) {
                return o;
            }
        };
        peopleMapper.listData(Wrappers.<People>query().last("limit 100000"), resultHandler);
        downloadProcessor.finish();
    }

    @SneakyThrows
    @ApiOperation(value = "download")
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(HttpServletResponse response){
        IPage page = new Page<People>(1, 100000, false);

        peopleMapper.pageList(page, Wrappers.query());
        DownloadUtil.downloadExcel("测试2.xls", response, page.getRecords(), People.class);
    }
}
