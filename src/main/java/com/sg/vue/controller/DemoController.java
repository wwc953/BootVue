package com.sg.vue.controller;

import com.sg.vue.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
