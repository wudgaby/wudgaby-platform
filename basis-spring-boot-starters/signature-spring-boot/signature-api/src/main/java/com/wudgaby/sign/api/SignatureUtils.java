package com.wudgaby.sign.api;

import cn.hutool.core.bean.copier.provider.BeanValueProvider;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.MD5;
import com.wudgaby.platform.utils.BeanUtils;
import com.wudgaby.platform.utils.ValidatorUtils;
import com.wudgaby.sign.supoort.BufferedHttpServletRequest;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName : SignatureUtils
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/19 17:54
 * @Desc :
 */
@Slf4j
@UtilityClass
public class SignatureUtils {
    public static void checkSignatureHeader(SignatureHeaders signatureHeaders){
        Optional<String> result = ValidatorUtils.validateEntity(signatureHeaders);
        if (result.isPresent()) {
            throw new SignatureException(result.get());
        }
        /*Assert.hasText(signatureHeaders.getAppKey(), "签名验证失败:APP_ID不能为空");
        Assert.hasText(signatureHeaders.getNonce(), "签名验证失败:NONCE不能为空");
        Assert.hasText(signatureHeaders.getTimestamp(), "签名验证失败:TIMESTAMP不能为空");
        Assert.hasText(signatureHeaders.getSignatureMethod(), "签名验证失败:SIGN_TYPE不能为空");
        Assert.hasText(signatureHeaders.getSignature(), "签名验证失败:SIGN不能为空");*/
        if (SignatureHeaders.SignMethodType.valueOf(SignatureHeaders.HEADER_SIGNATURE_METHOD) == null) {
            throw new IllegalArgumentException(String.format("签名验证失败:SIGN_TYPE必须为:%s,%s",
                    SignatureHeaders.SignMethodType.MD5, SignatureHeaders.SignMethodType.SHA256));
        }

        if (StringUtils.isBlank(signatureHeaders.getAppSecret())) {
            log.error("未找到appKey对应的appSecret, appKey= {}", signatureHeaders.getAppKey());
            throw new SignatureException("无效的appKey");
        }

        //其他合法性校验
        long now = System.currentTimeMillis();
        long requestTimestamp = Long.parseLong(signatureHeaders.getTimestamp());
        if ((now - requestTimestamp) > SignConst.EXPIRE_TIME) {
            String errMsg = "无效请求,请求时间超过规定范围.";
            log.error(errMsg);
            throw new SignatureException(errMsg);
        }


    }

    /**
     * 参考阿里云网关 使用摘要签名认证方式调用API
     * https://help.aliyun.com/document_detail/29475.html?spm=a2c4g.11186623.6.583.6e3e6280Yur1Hg
     * HTTPMethod HTTP的方法，全部大写，比如POST
     * Content-Type 请求中的Content-Type头的值，可为空
     *
     * Content-MD5
     * 请求中的Content-MD5头的值，可为空只有在请求存在Body且Body为非Form形式时才计算Content-MD5头，下面是Java的Content-MD5值的参考计算方式：
     * String content-MD5 = Base64.encodeBase64(MD5(bodyStream.getbytes("UTF-8")));
     *
     * Headers
     * HeaderKey1 + ":" + HeaderValue1 + "\n"\+
     * 参与签名计算的Header的Key按照字典排序后使用如下方式拼接
     *
     * PathAndParameters
     * Path + "?" + Key1 + "=" + Value1 + "&" + Key2 + "=" + Value2 + ... "&" + KeyN + "=" + ValueN
     * Query和Form参数对的Key按照字典排序后使用上面的方式拼接；
     * Query和Form参数为空时，则直接使用Path，不需要添加？；
     * 参数的Value为空时只保留Key参与签名，等号不需要再加入签名；
     * @param signatureVo
     * @return
     */
    public static String sign(SignatureVo signatureVo) {
        final StringBuilder sb = new StringBuilder();
        //构建http头
        sb.append(StringUtils.upperCase(StringUtils.trimToEmpty(signatureVo.getHttpMethod()))).append("\n");
        sb.append(StringUtils.trimToEmpty(signatureVo.getContentType())).append("\n");
        sb.append(StringUtils.trimToEmpty(signatureVo.getContentMD5())).append("\n");

        //构建ca请求头
        Map<String, String> signatureMap = signatureVo.getSignatureHeaders().toSignatureMap();
        signatureMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entity -> {
                    sb.append(entity.getKey()).append(":");
                    if (StringUtils.isNotBlank(entity.getValue())) {
                        sb.append(entity.getValue());
                    }
                    sb.append("\n");
                });

        //构建url
        sb.append(signatureVo.getPath());
        if (MapUtils.isNotEmpty(signatureVo.getParams())) {
            sb.append("?");

            signatureVo.getParams().entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(paramEntry -> {
                        String paramValue = String.join(",", Arrays.stream(paramEntry.getValue()).sorted().toArray(String[]::new));
                        sb.append(paramEntry.getKey());
                        if (StringUtils.isNotBlank(paramValue)) {
                            sb.append("=").append(paramValue);
                        }
                        sb.append("&");
                    });
        }

        String finalContent;
        if(sb.toString().endsWith("&")){
            finalContent = sb.deleteCharAt(sb.length()-1).toString();
        }else{
            finalContent = sb.toString();
        }

        //加密方式
        SignatureHeaders.SignMethodType signMethodType = null;
        if (StringUtils.isNotBlank(signatureVo.getSignatureHeaders().getSignatureMethod())) {
            signMethodType = SignatureHeaders.SignMethodType.valueOf(signatureVo.getSignatureHeaders().getSignatureMethod());
        }
        //默认MD5
        if (signMethodType == null) {
            signMethodType = SignatureHeaders.SignMethodType.MD5;
        }

        String signStr = "";
        switch (signMethodType) {
            case MD5:
                signStr = new HmacUtils(HmacAlgorithms.HMAC_MD5, signatureVo.getSecret()).hmacHex(finalContent);
                break;
            case SHA256:
                signStr = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, signatureVo.getSecret()).hmacHex(finalContent);
                break;
            default:
                break;
        }
        log.info("{} - {}", finalContent, signStr);
        return Base64.getEncoder().encodeToString(signStr.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public static SignatureVo buildSignatureVo(HttpServletRequest request, String appKey, String appSecret){
        SignatureHeaders signatureHeaders = SignatureHelper.generateSignatureHeaders(request);
        signatureHeaders.setAppKey(appKey);
        signatureHeaders.setAppSecret(appSecret);

        //获取body（对应@RequestBody）
        String contentMd5 = null;
        if (request instanceof BufferedHttpServletRequest) {
            String body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
            if(StringUtils.isNotBlank(body)){
                contentMd5 = buildContentMd5(body);
            }
        }

        //获取parameters（对应@RequestParam）
        Map<String, String[]> params = null;
        if (MapUtils.isNotEmpty(request.getParameterMap())) {
            params = request.getParameterMap();
        }

        //获取path variable（对应@PathVariable）
        /*String[] paths = null;
        ServletWebRequest webRequest = new ServletWebRequest(request, null);
        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (MapUtils.isNotEmpty(uriTemplateVars)) {
            paths = uriTemplateVars.values().toArray(new String[]{});
        }*/

        SignatureVo signatureVo = new SignatureVo();
        signatureVo.setHttpMethod(StringUtils.upperCase(request.getMethod()));
        signatureVo.setContentType(request.getContentType());
        signatureVo.setContentMD5(contentMd5);
        signatureVo.setSignatureHeaders(signatureHeaders);
        signatureVo.setPath(request.getRequestURI());
        signatureVo.setParams(params);
        signatureVo.setSecret(appSecret);

        return signatureVo;
    }

    public static String buildContentMd5(String body){
        return Base64.getEncoder().encodeToString(MD5.create().digest(body));
    }

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, StandardCharsets.UTF_8.name()) : null;
    }
}
