package com.wanling.types.common;

public class RedisKeys {
    public static String captcha(String key) {
        return "app:login:captcha:" + key;
    }
}
