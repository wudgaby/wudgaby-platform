package com.wudgaby.platform.sys.controller;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sys.dto.AttachmentDTO;
import com.wudgaby.platform.webcore.support.RequestContextHolderSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 文件信息表 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "文件管理")
@RestController
@RequestMapping("/files")
public class SysFileController {
    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public ApiResult<AttachmentDTO> commonUpload(@RequestParam("file") MultipartFile uploadFile) throws IOException {
        return ApiResult.success(buildAttachment(uploadFile));
    }

    @PostMapping("/uploads")
    @ApiOperation(value = "多文件上传")
    public ApiResult<List<AttachmentDTO>> commonUploads(@RequestParam("files") MultipartFile[] uploadFiles) throws IOException {
        List<AttachmentDTO> list = Lists.newArrayList();
        for(MultipartFile uploadFile : uploadFiles){
            list.add(buildAttachment(uploadFile));
        }
        return ApiResult.success(list);
    }


    @PostMapping("/download")
    @ApiOperation(value = "文件下载", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void commonDownload(@RequestParam String filePath) throws IOException {
        File downloadFile = new File(uploadPath + File.separator + filePath);
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);

        HttpServletResponse response = RequestContextHolderSupport.getResponse();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try(InputStream fis = new BufferedInputStream(new FileInputStream(downloadFile));
            OutputStream bos = new BufferedOutputStream(response.getOutputStream())){
            IOUtils.copy(fis, bos);
            bos.flush();
        }
    }

    private AttachmentDTO buildAttachment(MultipartFile uploadFile) throws IOException {
        String path = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);

        String newName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(uploadFile.getOriginalFilename());
        File file = new File(uploadPath + File.separator + path, newName);
        FileUtils.forceMkdirParent(file);
        uploadFile.transferTo(file);

        String fileUrl = path + File.separator + newName;
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setFileName(uploadFile.getOriginalFilename());
        attachmentDTO.setFileUrl(fileUrl);
        return attachmentDTO;
    }
}
