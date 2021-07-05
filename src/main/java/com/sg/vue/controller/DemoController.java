package com.sg.vue.controller;

import com.sg.vue.bean.ConvBean;
import com.sg.vue.bean.User;
import com.sg.vue.converter.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/index")
public class DemoController {

    @GetMapping("/init")
    public String init(Model model) {
        User user = new User();
        user.setName("hahah");
        user.setAge("0001");
        user.setAddr("中国");
        model.addAttribute("user", user);
        return "demo";
    }

    @GetMapping("/main")
    public String main(Model model) {
        return "main";
    }

    @PostMapping("/reve")
    @ResponseBody
    public ResponseResult<ConvBean> reve() {
        ConvBean convBean = new ConvBean();
        convBean.setType("01");
        ResponseResult<ConvBean> result = new ResponseResult<>();
        result.setCode("00000");
        result.setData(convBean);
        return result;
    }

    @PostMapping("/reveList")
    @ResponseBody
    public ResponseResult<List<ConvBean>> reveList() {
        List<ConvBean> list = new ArrayList<>();
        ConvBean convBean = new ConvBean();
        convBean.setType("01");
        convBean.setTestPerson("11022");
        list.add(convBean);
        ConvBean convBean2 = new ConvBean();
        convBean2.setType("02");
        convBean2.setTestPerson("22099");
        list.add(convBean2);
        ResponseResult<List<ConvBean>> result = new ResponseResult<>();
        result.setCode("00000");
        result.setData(list);
        return result;
    }

}
