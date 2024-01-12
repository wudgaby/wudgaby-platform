package com.wudgaby.sample.oauth2resource01;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/8 0008 18:15
 * @desc :
 */
@RestController
public class ResController {

    @GetMapping("/res1")
    public String res1() {
        return "server-1-res1";
    }

    @GetMapping("/res2")
    public String res12() {
        return "server-1-res2";
    }
}
