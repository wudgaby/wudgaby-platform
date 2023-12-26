package com.wudgaby.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/12/25 0025 18:45
 * @desc :
 */
@RestController
public class I18nController {

    @Autowired
    private MessageUtils messageUtils;

    @GetMapping("/")
    public String i18n(){
        return messageUtils.getMessage("success") + ":" + messageUtils.getMessage("failed");
    }

}
