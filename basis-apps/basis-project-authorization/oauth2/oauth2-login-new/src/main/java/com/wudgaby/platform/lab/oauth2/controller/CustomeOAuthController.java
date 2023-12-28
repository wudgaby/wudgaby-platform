package com.wudgaby.platform.lab.oauth2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/9/23 15:13
 * @Desc :
 */
@Slf4j
@Controller
public class CustomeOAuthController {
    @GetMapping("/oauth2/login")
    public String oauth2LoginPage(){
        return "oauth2login";
    }

    @GetMapping("/home")
    public String home(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient, Model model, Authentication authentication){
        log.info(oAuth2AuthorizedClient.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        model.addAttribute("name", attributes.get("name"));
        return "home";
    }
}
