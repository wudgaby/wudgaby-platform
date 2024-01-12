package com.wudgaby.sample.oauth2resource01;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/8 0008 18:15
 * @desc :
 */
@RestController
public class ResController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/res1")
    public String res1(HttpServletRequest request) {
        return "server-2-res1";
        //return getServer("http://127.0.0.1:9001/res2", request);
    }

    @GetMapping("/res2")
    public String res12() {
        return "server-2-res2";
    }

    @GetMapping("/res3")
    public String res3(HttpServletRequest request) {
        return getServer("http://127.0.0.1:9001/res2", request);
    }

    /**
     * 请求资源
     *
     * @param url
     * @param request
     * @return
     */
    private String getServer(String url,
                             HttpServletRequest request) {
        // ======== 1、从session中取token ========
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("micro-token");

        // ======== 2、请求token ========
        // 先查session中是否有token；session中没有
        if (StringUtils.isEmpty(token)) {
            // ===== 去认证中心申请 =====
            // 对id及密钥加密
            byte[] userpass = Base64.encodeBase64(("micro_service:123456").getBytes());
            String str = "";
            try {
                str = new String(userpass, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("grant_type", "client_credentials");
            //multiValueMap.add("scope", "all export");

            // 请求头
            HttpHeaders headers1 = new HttpHeaders();
            // 组装请求头
            headers1.add("Authorization", "Basic " + str);
            // 请求体
            HttpEntity<Object> httpEntity1 = new HttpEntity<>(multiValueMap, headers1);

            // 响应体
            ResponseEntity<String> responseEntity1 = null;
            try {
                // 发起申请令牌请求
                responseEntity1 = restTemplate.exchange("http://ssoserver.cn:8080/oauth2/token", HttpMethod.POST, httpEntity1, String.class);
            } catch (RestClientException e) {
                //
                System.out.println("令牌申请失败");
            }

            // 令牌申请成功
            if (responseEntity1 != null) {
                // 解析令牌
                //String t = JSON.parseObject(responseEntity1.getBody(), MyAuth.class).getAccess_token();
                Map<String, String> resMap = JSONUtil.toBean(responseEntity1.getBody(), HashMap.class);
                String t = resMap.get("access_token");
                // 存入session
                session.setAttribute("micro-token", t);
                // 赋于token变量
                token = t;
            }
        }

        // ======== 3、请求资源 ========
        // 请求头
        HttpHeaders headers2 = new HttpHeaders();
        // 组装请求头
        headers2.add("Authorization", "Bearer " + token);
        // 请求体
        HttpEntity<Object> httpEntity2 = new HttpEntity<>(headers2);
        // 响应体
        ResponseEntity<String> responseEntity2;
        try {
            // 发起访问资源请求
            responseEntity2 = restTemplate.exchange(url, HttpMethod.GET, httpEntity2, String.class);
        } catch (RestClientException e) {
            // 令牌失效(认证失效401) --> 清除session
            // e.getMessage() 信息格式：
            // 401 : "{"msg":"认证失败","uri":"/res2"}"
            String str = e.getMessage();
            // 判断是否含有 401
            if(StrUtil.contains(str, "401")){
                // 如果有401，把session中 micro-token 的值设为空
                session.setAttribute("micro-token","");
            }
            // 取两个括号中间的部分（包含两个括号）
            return str.substring(str.indexOf("{"), str.indexOf("}") + 1);
        }
        // 返回
        return responseEntity2.getBody();
    }
}
