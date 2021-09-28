package com.sg.vue.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sg.vue.user.model.PeopleQueryAO;
import com.sg.vue.user.model.People;
import com.sg.vue.user.service.UserServiceImpl;
import com.sg.vue.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserServiceImpl userService;

    @PostMapping("/queryUserList")
    public Map queryUserList(@RequestBody PeopleQueryAO queryAO) {
        Map reult = new HashMap<>();
        reult.put("list", userService.queryUserList(queryAO));
        reult.put("total", userService.queryUserListCount(queryAO));
        return reult;
    }

    @PostMapping(value = "/saveUser")
    public Integer saveUser(@RequestBody People people) {
        return userService.saveUser(people);
    }

    @PostMapping(value = "/delUser")
    public Integer delUser(@RequestBody People people) {
        return userService.delUser(people);
    }

    @PostMapping("/login")
    public JSONObject login(@RequestBody People people) {
        JSONObject result = new JSONObject();
        People people1 = userService.queryPeople(people);
        result.put("userInfo", people1);
        result.put("token", TokenUtils.getToken());
        return result;
    }

}
