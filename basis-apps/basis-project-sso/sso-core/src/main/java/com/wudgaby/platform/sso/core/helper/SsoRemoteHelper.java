package com.wudgaby.platform.sso.core.helper;

import cn.hutool.core.net.url.UrlBuilder;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.vo.PermissionVo;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName : SsoRemoteHelper
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/11/18 1:27
 * @Desc :   
 */
@Component
@Slf4j
public class SsoRemoteHelper {
    @Autowired
    private SsoProperties ssoProperties;
    @Autowired
    private RestTemplate ssoServerHttpClient;

    @Bean
    public RestTemplate ssoServerHttpClient(){
        return new RestTemplate();
    }

    @Retryable(value = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(2000))
    public SsoUserVo remoteCheck(String token, boolean isWeb){
        String checkUrl = UrlBuilder.of(ssoProperties.getServer() + (isWeb ? "" : "/app") + SsoConst.SSO_CHECK_URL, Charsets.UTF_8)
                .addQuery(SsoConst.TOKEN, token)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(SsoConst.SSO_HEADER_X_TOKEN, token);
        //headers.add("cookie", SsoConst.SSO_SESSION_ID + "=" +token);
        try{
            ResponseEntity<ApiResult> response = ssoServerHttpClient.exchange(
                    checkUrl,
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    ApiResult.class);

            ApiResult apiResult = response.getBody();
            if(ApiResult.isSuccess(apiResult)){
                return JacksonUtil.deserialize(JacksonUtil.serialize2Bytes(apiResult.getData()), SsoUserVo.class);
            }
        }catch (RestClientException ioe){
            log.error(ioe.getMessage(), ioe);
            throw new RetryException("retry");
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(2000))
    public ApiResult codeExToken(String code, String sign, String appSecret){
        String getTokenUrl = UrlBuilder.of(ssoProperties.getServer() + "/app" + SsoConst.SSO_TOKEN_URL, Charsets.UTF_8)
                .addQuery("code", code)
                .addQuery("secret", appSecret)
                .addQuery("sign", sign)
                .build();

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<ApiResult> response = ssoServerHttpClient.exchange(
                getTokenUrl,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                ApiResult.class);

        return response.getBody();
    }

    @Retryable(value = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(2000))
    public List<PermissionVo> getUserResource(String sessionId, String userId, String sysCode){
        String checkUrl = UrlBuilder.of(ssoProperties.getServer() + SsoConst.SSO_USER_RESOURCE_URL, Charsets.UTF_8)
                .addQuery("sysCode", sysCode)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(SsoConst.SSO_HEADER_X_TOKEN, sessionId);
        try{
            ResponseEntity<ApiResult> response = ssoServerHttpClient.exchange(
                    checkUrl,
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    ApiResult.class);

            ApiResult apiResult = response.getBody();
            if(ApiResult.isSuccess(apiResult)){
                List<PermissionVo> permissionVoList = JacksonUtil.deserializeArray(JacksonUtil.serialize(apiResult.getData()), PermissionVo.class);
                return Optional.ofNullable(permissionVoList).orElse(Lists.newArrayList());
            }
        }catch (RestClientException ioe){
            log.error(ioe.getMessage(), ioe);
            throw new RetryException("retry");
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
        }
        return Lists.newArrayList();
    }
}
