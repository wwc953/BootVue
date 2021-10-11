package com.sg.vue.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.sg.vue.utils.JsonUtil;
import com.sg.vue.websocket.SendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired(required = false)
    RedisTemplate redisTemplate;

    @Autowired(required = false)
    ChannelTopic webMsgChannelTopic;


    @GetMapping(value = "/sendTopic/{message}")
    public String sendTopic(@PathVariable String message) {
        String messageId = String.valueOf(UUID.randomUUID()).replace("-", "");
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, String> params = new HashMap<>();
//        JSONObject params = new JSONObject();
        params.put("messageId", messageId);
        params.put("messageData", message);
        params.put("createTime", createTime);
        redisTemplate.convertAndSend(webMsgChannelTopic.getTopic(), JSONObject.toJSONString(params));
        return "send1 success";
    }

    @PostMapping(value = "/sendWebMsg")
    public SendDTO sendWebMsg(@RequestBody SendDTO sendDTO) {
        String messageId = String.valueOf(UUID.randomUUID()).replace("-", "");
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        sendDTO.setSendTime(createTime);
        sendDTO.setMsgID(messageId);
        redisTemplate.convertAndSend(webMsgChannelTopic.getTopic(), sendDTO);
        return sendDTO;
    }
}
