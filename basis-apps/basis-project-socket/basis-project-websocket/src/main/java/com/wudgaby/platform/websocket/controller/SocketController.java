package com.wudgaby.platform.websocket.controller;

import com.wudgaby.platform.websocket.consts.SysConsts;
import com.wudgaby.platform.websocket.storage.UserStorage;
import com.wudgaby.platform.websocket.vo.FastPrincipal;
import com.wudgaby.platform.websocket.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/26 3:16
 * @Desc :   
 */
@Controller
public class SocketController {

    @Autowired
    private UserStorage userStorage;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(HttpServletRequest request, @RequestParam String account, @RequestParam String password) {
        UserVo userVo = userStorage.find(account, password);
        if(userVo != null){
            request.getSession().setAttribute(SysConsts.SESSION_USER, new FastPrincipal(userVo.getName()));
            return "index";
        }
        request.setAttribute("errorMsg", "账号或密码错误.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/index";
    }

    @GetMapping("/ws/javax")
    public String javaxwebsocket() {
        return "javax-websocket";
    }

    @GetMapping("/ws/spring")
    public String springwebsocket() {
        return "spring-websocket";
    }

    @GetMapping("/ws/stomp")
    public String stomp() {
        return "stomp";
    }
}
