package com.wudgaby.platform.webcore.filter.security.xss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Vector;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/10/5/005 19:58
 * @Desc :
 */
@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 是否是上传
     */
    private boolean isUpData = false;
    private HttpServletRequest orgRequest = null;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        String contentType = request.getContentType();
        if (null != contentType) {
            isUpData = contentType.startsWith("multipart");
        }
        this.orgRequest = request;
    }

    @Override
    public String getQueryString() {
        String queryString = super.getQueryString();
        return Optional.ofNullable(queryString).map(val -> HtmlUtils.htmlEscape(val)).orElse(queryString);
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        return Optional.ofNullable(header).map(val -> HtmlUtils.htmlEscape(val)).orElse(header);
    }

    /**
     * 处理@RequestHeader中的xss
     * @param name
     * @return
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        Enumeration<String> headers = super.getHeaders(name);
        Vector<String> newHeaders = new Vector<>();
        while (headers.hasMoreElements()) {
            newHeaders.add(HtmlUtils.htmlEscape(headers.nextElement()));
        }
        return newHeaders.elements();
    }

    @Override
    public String getParameter(String name) {
        String param = super.getParameter(name);
        return Optional.ofNullable(param).map(val -> HtmlUtils.htmlEscape(val)).orElse(param);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null && values.length > 0) {
            int length = values.length;
            String[] escapeValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapeValues[i] = Optional.ofNullable(values[i]).map(val -> HtmlUtils.htmlEscape(val)).orElse(values[i]);
            }
            return escapeValues;
        }
        return super.getParameterValues(name);
    }

    /**
     * 处理 @RequestBody中的xss
     * 无法正确处理json
     * @return
     * @throws IOException
     */
    /*@Override
    public ServletInputStream getInputStream() throws IOException {
        if(isUpData){
            return super.getInputStream();
        }
        ServletInputStream inputStream = null;
        String body = getBodyString(super.getInputStream());
        if (StringUtils.isNotEmpty(body)) {
            Map<String, Object> paramMap = JSON.parseObject(body);
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                //paramMap.put(HtmlUtils.htmlEscape(entry.getKey()), HtmlUtils.htmlEscape(entry.getValue().toString()));
                paramMap.put(entry.getKey(), HtmlUtils.htmlEscape(entry.getValue().toString()));
            }
            body = JSON.toJSONString(paramMap);
            inputStream = new PostServletInputStream(body);
        }
        return inputStream;
    }*/

    @Override
    public BufferedReader getReader() throws IOException {
        String enc = getCharacterEncoding();
        if(enc == null || "".equals(enc)) {
            enc = "UTF-8";
        }
        return new BufferedReader(new InputStreamReader(getInputStream(), enc));
    }

    public static String getBodyString(InputStream inputStream) throws IOException {
        StringBuffer sb = new StringBuffer();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString().replaceAll("\\s", "");
    }

    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }
}
