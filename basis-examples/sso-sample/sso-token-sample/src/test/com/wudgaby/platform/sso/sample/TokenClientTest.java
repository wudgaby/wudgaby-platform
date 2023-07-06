package com.wudgaby.platform.sso.sample;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * @ClassName : IndexControllerTest
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/25 2:21
 * @Desc :
 */
@Slf4j
public class TokenClientTest {
    public static String ssoServer = "http://localhost:9900";
    public static String client01 = "http://localhost:9910";
    public static String client02 = "http://localhost:9911";

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    void test() throws Exception {
        String sessionId = login();
        Assert.assertNotNull(sessionId);

        String username = loginCheck(sessionId);
        Assert.assertNotNull(username);

        clientApiRequest(client01, sessionId);
        clientApiRequest(client02, sessionId);

        boolean loginoutResult = logout(sessionId);
        Assert.assertTrue(loginoutResult);

        // 登陆状态校验
        username = loginCheck(sessionId);
        Assert.assertNull(username);

        clientApiRequest(client01, sessionId);
        clientApiRequest(client02, sessionId);
    }

    private void clientApiRequest(String clientApiUrl, String sessionId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SsoConst.SSO_HEADER_X_TOKEN, sessionId);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(clientApiUrl, HttpMethod.GET, requestEntity, ApiResult.class );

        ApiResult apiResult = responseEntity.getBody();
        log.info("{}", apiResult);
        if(apiResult.getSuccess()){
            SsoUserVo ssoUserVo = JacksonUtil.deserialize(JacksonUtil.serialize(apiResult.getData()), SsoUserVo.class);
            log.info("登录用户: {}", ssoUserVo);
        }else{
            log.info("请求失败. {}", apiResult.getMessage());
        }
    }

    private String login() throws Exception {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("account", "wudgaby");
        paramMap.add("password", "aa0000");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);

        ResponseEntity<ApiResult> responseEntity = restTemplate.postForEntity(ssoServer + "/app" + SsoConst.SSO_LOGIN_URL, httpEntity, ApiResult.class);

        ApiResult apiResult = responseEntity.getBody();
        log.info("{}", apiResult);
        if(ApiResult.isSuccess(apiResult)){
            return String.valueOf(apiResult.getData());
        }
        return null;
    }

    private boolean logout(String sessionId) throws Exception {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("sessionId", sessionId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ssoServer + "/app" + SsoConst.SSO_LOGOUT_URL);
        URI uri = builder.queryParams(paramMap).build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(uri,
                                                        HttpMethod.DELETE, httpEntity, ApiResult.class);

        ApiResult apiResult = responseEntity.getBody();
        log.info("{}", apiResult);
        return apiResult.getSuccess();
    }

    private String loginCheck(String sessionId) throws Exception {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("sessionId", sessionId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ssoServer + "/app" + SsoConst.SSO_CHECK_URL);
        URI uri = builder.queryParams(paramMap).build().encode().toUri();

        ResponseEntity<ApiResult> responseEntity = restTemplate.getForEntity(uri ,ApiResult.class);

        ApiResult apiResult = responseEntity.getBody();
        log.info("{}", apiResult);
        if(apiResult.getSuccess()){
            SsoUserVo ssoUserVo = JacksonUtil.deserialize(JacksonUtil.serialize(apiResult.getData()), SsoUserVo.class);
            return ssoUserVo.getUsername();
        }

        return null;
    }
}