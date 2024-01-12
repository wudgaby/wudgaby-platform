package com.wudgaby.sample.oauth2server.oauth2extend;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/11 0011 17:57
 * @desc :
 */
@Service
public class GiteeOAuth2UserService extends DefaultOAuth2UserService {
    @Resource
    private RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /*if(userRequest.getClientRegistration().getRegistrationId().equalsIgnoreCase("gitee")){
            return loadUserForGitee(userRequest);
        }*/
        return super.loadUser(userRequest);
    }

    private OAuth2User loadUserForGitee(OAuth2UserRequest userRequest){
        String accessToken = userRequest.getAccessToken().getTokenValue();
        System.out.println(accessToken);

        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);

        String url = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri() +
                "?access_token={access_token}";

        return restTemplate.exchange(url, HttpMethod.GET, entity, GiteeOAuth2User.class, params).getBody();
    }
}
