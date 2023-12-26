package com.wudgaby.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/12/25 0025 18:38
 * @desc :
 */
@Component
@RequiredArgsConstructor
public class MessageUtils {
    private final MessageSource messageSource;

    public String getMessage(String message){
        return messageSource.getMessage(message, null,  LocaleContextHolder.getLocale());
    }

    public String getMessage(String message, Object[] args){
        return messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
    }

    public String getMessage(String message, Object[] args, Locale locale){
        return messageSource.getMessage(message, args, locale);
    }
}
