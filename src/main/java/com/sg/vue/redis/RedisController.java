package com.sg.vue.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired(required = false)
    StringRedisTemplate stringredisTemplate;

    @Autowired(required = false)
    ChannelTopic webMsgChannelTopic;

    @GetMapping(value = "/sendTopic/{message}")
    public String sendTopic(@PathVariable String message) {
        stringredisTemplate.convertAndSend(webMsgChannelTopic.getTopic(), message);
        return "send1 success";
    }
}
