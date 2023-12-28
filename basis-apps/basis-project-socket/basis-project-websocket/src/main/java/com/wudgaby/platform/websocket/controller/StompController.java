package com.wudgaby.platform.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/26 5:08
 * @Desc :   https://www.cnblogs.com/jmcui/p/8999998.html
 */
@Slf4j
@Controller
public class StompController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    private Pattern pattern = Pattern.compile("\\@(\\S+)");

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Exception exception) {
        return exception.getMessage();
    }

    @MessageMapping("/hello")
    public Object hello(String message){
        log.info("接收消息: {}", message);
        return message;
    }

    @MessageMapping("/greetings")
    @SendTo("/topic/greetings")
    public Object greetings(String message){
        log.info("接收消息: {}", message);
        return message;
    }

    /**
     * 发送到/user/{token}/chat
     */
    @MessageMapping("/send2User")
    @SendToUser("/topic/chat")
    public Object send2User(String message, Principal principal){
        log.info("接收消息: {} - {}", principal, message);

        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String username = matcher.group(1);
            simpMessagingTemplate.convertAndSendToUser(username, "/topic/chat", principal.getName() + " @了你." + message);
        }

        return message;
    }

    /**
     * 一次性的都请求响应式
     * @param principal
     * @return
     */
    @SubscribeMapping("/subscribe")
    public String handleSubscribe(Principal principal) {
        return "server subscribes " + principal;
    }

    @GetMapping("/stomp/send2User")
    @ResponseBody
    public Object sendMsgByUser(@RequestParam String name, @RequestParam String msg) {
        //发送到/user/{token}/sub
        simpMessagingTemplate.convertAndSendToUser(name, "/topic/sub", msg);
        return "success";
    }

    @GetMapping("/stomp/send2All")
    @ResponseBody
    public Object sendMsgByAll(@RequestParam String msg) {
        simpMessagingTemplate.convertAndSend("/topic/sub", msg);
        simpMessagingTemplate.convertAndSend("/app/subscribe", msg);
        return "success";
    }
}
