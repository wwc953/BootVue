package com.sg.vue.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.sg.vue.cache.CaffeineCache;
import com.sg.vue.converter.ResponseResult;
import com.sg.vue.role.BootRoleServer;
import com.sg.vue.role.model.BootRoleResultVO;
import com.sg.vue.user.model.PeopleQueryAO;
import com.sg.vue.user.model.People;
import com.sg.vue.user.service.UserServiceImpl;
import com.sg.vue.utils.BootCodes;
import com.sg.vue.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserServiceImpl userService;

    @Resource
    BootRoleServer roleServer;

    @Resource
    CaffeineCache caffeineCache;

    @PostMapping("/queryUserList")
    public ResponseResult queryUserList(@RequestBody PeopleQueryAO queryAO) {
        return ResponseResult.success(userService.queryUserList(queryAO), userService.queryUserListCount(queryAO));
    }

    @PostMapping(value = "/saveUser")
    public ResponseResult saveUser(@RequestBody People people) {
        return ResponseResult.success(userService.saveUser(people));
    }

    @PostMapping(value = "/delUser")
    public ResponseResult delUser(@RequestBody People people) {
        return ResponseResult.success(userService.delUser(people));
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody People people) {
        JSONObject result = new JSONObject();
        People userinfo = userService.queryPeople(people);
        if (userinfo == null) {
            return ResponseResult.fail(BootCodes.errorcode, "用户名、密码错误！！！");
        }
        result.put("userInfo", userinfo);
        String token = TokenUtils.getToken();
        List<BootRoleResultVO> bootRoleResultVOS = roleServer.selectRoleByUserId(userinfo.getId());
        result.put("token", token);
        result.put("urlListData", bootRoleResultVOS);
        caffeineCache.hset("localtoken", token, bootRoleResultVOS);
        return ResponseResult.success(result);
    }

}
