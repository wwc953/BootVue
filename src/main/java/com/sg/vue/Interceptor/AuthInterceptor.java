package com.sg.vue.Interceptor;

import com.alibaba.fastjson.JSONObject;
import com.sg.vue.cache.CaffeineCache;
import com.sg.vue.converter.ResponseResult;
import com.sg.vue.exception.CheckTokenException;
import com.sg.vue.utils.BootCodes;
import com.sg.vue.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 拦截器，判断用户是否登录，token是否为空
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    CaffeineCache caffeineCache;

    @Value("${open.token:false}")
    private String openToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String token = request.getHeader("token");
        if (!"true".equalsIgnoreCase(openToken)) {
            return true;
        }

        boolean b = false;
        try {
            b = TokenUtils.checkToken(token);
        } catch (CheckTokenException e) {
            e.printStackTrace();
            ResponseResult<Object> fail = ResponseResult.fail(BootCodes.errorcode, "非法token，请重新登录");
            response.getWriter().print(JSONObject.toJSONString(fail));
            return false;
        }
        if (!b) {
            ResponseResult<Object> fail = ResponseResult.fail(BootCodes.nologincode, "非法token，请重新登录");
            response.getWriter().print(JSONObject.toJSONString(fail));
            return false;
        }
        Object loginStatus = caffeineCache.hget("localtoken", token);
//        Object loginStatus = redisService.get(token);
        if (Objects.isNull(loginStatus)) {
            ResponseResult<Object> fail = ResponseResult.fail(BootCodes.nologincode, "token错误，请重新登录");
            response.getWriter().print(JSONObject.toJSONString(fail));
            return false;
        }
        return true;
    }
}
