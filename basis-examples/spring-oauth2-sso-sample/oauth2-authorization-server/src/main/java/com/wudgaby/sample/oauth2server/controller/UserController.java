package com.wudgaby.sample.oauth2server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/9 0009 10:24
 * @desc :
 */
@RestController
public class UserController {

    /**
     * Successful UserInfo Response
     * {
     *    "sub": "248289761001",
     *    "name": "Jane Doe",
     *    "given_name": "Jane",
     *    "family_name": "Doe",
     *    "preferred_username": "j.doe",
     *    "email": "janedoe@example.com",
     *    "picture": "http://example.com/janedoe/me.jpg"
     *   }
     * @return
     */
    @GetMapping("/oauth2/user")
    public Authentication getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw new RuntimeException("无有效认证用户！");
        }
        return authentication;
    }

    @GetMapping("/oauth2/user2")
    public Map<String,Object> getUser2(Principal principal){
        if ((principal instanceof JwtAuthenticationToken)) {
            return ((JwtAuthenticationToken)principal).getToken().getClaims();
        }

        if(principal instanceof OAuth2AuthenticationToken){
            return ((OAuth2AuthenticationToken)principal).getPrincipal().getAttributes();
        }

        return Collections.emptyMap();
    }

}
