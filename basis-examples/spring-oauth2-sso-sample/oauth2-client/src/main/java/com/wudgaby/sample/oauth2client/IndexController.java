package com.wudgaby.sample.oauth2client;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/10 0010 9:42
 * @desc :
 */
@Controller
public class IndexController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/")
    public String index(Model model) {
        // 从安全上下文中获取登录信息，返回给model
        Map<String, Object> map = new HashMap<>(2);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        map.put("name", auth.getName());

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        List<? extends GrantedAuthority> authoritiesList = authorities.stream().collect(Collectors.toList());
        map.put("authorities", authoritiesList);

        model.addAttribute("user", JSONUtil.toJsonStr(map));
        return "index";
    }

    @GetMapping("/ssologout")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) {

        // ========== 清理客户端 ===========
        // 清理客户端session
        request.getSession().invalidate();
        // 清理客户端安全上下文
        SecurityContextHolder.clearContext();

        // ========== 清理认证中心 ===========
        // 跳转至认证中心退出页面
        try {
            response.sendRedirect("http://ssoserver.cn:8080/ssologout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
