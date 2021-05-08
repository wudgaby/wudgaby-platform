package com.wudgaby.oauth2.uaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : ClientDetailsController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/24 14:56
 * @Desc :   TODO
 */
@RestController
public class ClientDetailsController {
    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    @PostMapping("/clientDetails")
    public String addClientDetail(ClientDetails clientDetails){
        /*BaseClientDetails baseClientDetails = new BaseClientDetails("user-service", "", "service", "refresh_token, password", "", "");
        baseClientDetails.setClientSecret("123456");
        baseClientDetails.setAccessTokenValiditySeconds(3600);*/
        jdbcClientDetailsService.addClientDetails(clientDetails);
        return "success";
    }
}
