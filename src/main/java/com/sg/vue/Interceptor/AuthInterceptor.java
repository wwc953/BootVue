package com.sg.vue.Interceptor;

import com.alibaba.fastjson.JSONObject;
import com.sg.vue.converter.ResponseResult;
import com.sg.vue.utils.BootCodes;
import com.sg.vue.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器，判断用户是否登录，token是否为空
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String token = request.getHeader("token");
        if (!TokenUtils.checkToken(token)) {
            ResponseResult<Object> fail = ResponseResult.fail(BootCodes.nologincode, "非法token，请重新登录");
            response.getWriter().print(JSONObject.toJSONString(fail));
            return false;
        }
//        Object loginStatus = redisService.get(token);
//        if( Objects.isNull(loginStatus)){
//            response.getWriter().print("token错误，请查看！");
//            return false;
//        }
        return true;
    }
}
