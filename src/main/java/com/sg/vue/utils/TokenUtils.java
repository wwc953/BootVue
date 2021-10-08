package com.sg.vue.utils;

import com.sg.vue.exception.CheckTokenException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * token 工具类
 */
public class TokenUtils {

    public static String TOKEN_FLAG = "vueboot";

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        String token = UUID.randomUUID().toString().replace("-", "") + "-" + TOKEN_FLAG;
        try {
            return AesUtil.encrypt(token, "token");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取token失败");
        }

    }

    /**
     * 校验token是否合法
     *
     * @param token
     * @return
     */
    public static boolean checkToken(String token) throws CheckTokenException{
        if (token == null || token.trim().length() == 0) {
            return false;
        }
        String decryptstr = null;
        try {
            decryptstr = AesUtil.decrypt(token, "token");
        } catch (Exception e) {
            throw new CheckTokenException(e);
        }
        String flag = decryptstr.split("-")[1];
        return TOKEN_FLAG.equalsIgnoreCase(flag);
    }

}
