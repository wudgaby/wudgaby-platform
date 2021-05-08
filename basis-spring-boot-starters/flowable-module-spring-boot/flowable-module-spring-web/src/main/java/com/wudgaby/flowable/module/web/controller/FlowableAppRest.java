package com.wudgaby.flowable.module.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.idm.api.User;
import org.flowable.ui.common.model.RemoteUser;
import org.flowable.ui.common.security.DefaultPrivileges;
import org.flowable.ui.common.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : FlowableAppRest
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/5 15:11
 * @Desc :   TODO
 */
@RestController
@RequestMapping("/app/rest/")
public class FlowableAppRest {
    @Autowired protected ObjectMapper objectMapper;

    /*@GetMapping("authenticate")
    public Map<String, Object> getAuthenticate() {
        Map<String, Object> map = new HashMap<>();
        map.put("login", "admin");
        return map;
    }*/

    /**
     * 账号信息
     */
    @GetMapping("account")
    public User getAccount() {
        User user = getCurrentUser();
        //放入线程
        SecurityUtils.assumeUser(user);
        return user;
    }

    private User getCurrentUser(){
        RemoteUser user = new RemoteUser();
        user.setId("admin");
        user.setDisplayName("admin");
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setEmail("admin@admin.com");
        user.setPassword("test");

        List<String> pris = new ArrayList<>();
        pris.add(DefaultPrivileges.ACCESS_MODELER);
        pris.add(DefaultPrivileges.ACCESS_IDM);
        pris.add(DefaultPrivileges.ACCESS_ADMIN);
        pris.add(DefaultPrivileges.ACCESS_TASK);
        pris.add(DefaultPrivileges.ACCESS_REST_API);
        user.setPrivileges(pris);

        return user;
    }
}
