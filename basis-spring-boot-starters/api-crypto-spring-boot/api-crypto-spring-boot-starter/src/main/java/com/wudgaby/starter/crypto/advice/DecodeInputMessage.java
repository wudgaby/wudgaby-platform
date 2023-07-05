package com.wudgaby.starter.crypto.advice;

import com.wudgaby.starter.crypto.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.*;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/5 0005 16:40
 * @desc :
 */
@Slf4j
public class DecodeInputMessage implements HttpInputMessage {
    private HttpHeaders headers;
    private InputStream body;

    public DecodeInputMessage(HttpInputMessage httpInputMessage) {
        this.headers = httpInputMessage.getHeaders();
        try {
            String encodeAesContent = new BufferedReader(new InputStreamReader(httpInputMessage.getBody())).lines().collect(Collectors.joining(System.lineSeparator()));
            String replaced = encodeAesContent.replace("\"", "");
            String decoded = AESUtil.decrypt(replaced);
            if (!StringUtils.isEmpty(decoded)) {
                this.body = new ByteArrayInputStream(decoded.getBytes());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("参数解密失败.");
        }
    }

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}