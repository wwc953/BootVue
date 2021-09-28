package com.sg.vue.utils;

import java.util.UUID;

public class TokenUtils {
    public static String getToken(){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

}
