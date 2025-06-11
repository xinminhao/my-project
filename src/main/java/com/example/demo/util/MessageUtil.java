package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {

    private static MessageSource messageSource;

    @Autowired
    public MessageUtil(MessageSource messageSource) {
        MessageUtil.messageSource = messageSource;
    }

    /**
     * 获取国际化消息（默认系统语言）
     */
    public static String get(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }

    /**
     * 获取国际化消息（指定语言）
     */
    public static String get(Locale locale, String key, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }
}

