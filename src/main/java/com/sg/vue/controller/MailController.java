package com.sg.vue.controller;

import com.sg.vue.converter.ResponseResult;
import com.sg.vue.model.ao.MailDTO;
import com.sg.vue.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    MailService mailService;

    @PostMapping("/send")
    public ResponseResult<String> send(@RequestBody MailDTO mailDTO) {
        return mailService.sendMail(mailDTO);
    }

}
