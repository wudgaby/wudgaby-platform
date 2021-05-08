package com.wudgaby.sign.api;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName : SignatureHeaders
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/19 17:45
 * @Desc :   TODO
 */
@Signature
@Data
public class SignatureHeaders {
    public static final String SIGNATURE_HEADERS_PREFIX = "x-ca";

    public static final Set<String> HEADER_NAME_SET = Sets.newHashSet();

    public static final String HEADER_APP_KEY = SIGNATURE_HEADERS_PREFIX + "-appKey";
    public static final String HEADER_TIMESTAMP = SIGNATURE_HEADERS_PREFIX + "-timestamp";
    public static final String HEADER_NONCE = SIGNATURE_HEADERS_PREFIX + "-nonce";
    public static final String HEADER_SIGNATURE = SIGNATURE_HEADERS_PREFIX + "-signature";

    //private static final String HEADER_SIGNATURE_METHOD = SIGNATURE_HEADERS_PREFIX + "signatureMethod";

    static {
        HEADER_NAME_SET.add(HEADER_APP_KEY);
        HEADER_NAME_SET.add(HEADER_TIMESTAMP);
        HEADER_NAME_SET.add(HEADER_NONCE);
        HEADER_NAME_SET.add(HEADER_SIGNATURE);

        //HEADER_NAME_SET.add(HEADER_SIGNATURE_METHOD);
    }

    /**
     * 线下分配的值
     * 客户端和服务端各自保存appKey对应的appSecret
     */
    @NotBlank(message = "Header中缺少" + HEADER_APP_KEY)
    @SignatureField
    private String appKey;
    /**
     * 线下分配的值
     * 客户端和服务端各自保存，与appKey对应
     */
    @SignatureField
    private String appSecret;
    /**
     * 时间戳，单位: ms
     */
    @NotBlank(message = "Header中缺少" + HEADER_TIMESTAMP)
    @SignatureField
    private String timestamp;
    /**
     * 流水号【防止重复提交】; (备注：针对查询接口，流水号只用于日志落地，便于后期日志核查； 针对办理类接口需校验流水号在有效期内的唯一性，以避免重复请求)
     */
    @NotBlank(message = "Header中缺少" + HEADER_NONCE)
    @SignatureField
    private String nonce;
    /**
     * 签名
     */
    @NotBlank(message = "Header中缺少" + HEADER_SIGNATURE)
    private String signature;


    /**
     * 签名算法类型
     */
    /*@NotBlank(message = "Header中缺少" + SIGNATURE_HEADERS_PREFIX)
    @SignatureField
    private String signatureMethod;*/

    public Map<String, String> toSignatureMap(){
        Map<String, String> result = Maps.newHashMap();
        result.put(HEADER_APP_KEY, this.getAppKey());
        result.put(HEADER_TIMESTAMP, this.getTimestamp());
        result.put(HEADER_NONCE, this.getNonce());
        //result.put(HEADER_SIGNATURE, this.getSignature());
        return result;
    }

}