package com.sg.vue.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/wb")
public class WebSocketController {

    @Autowired
    WebSocketServer webSocket;

    @GetMapping("/sendAllWebSocket")
    public String test() {
        JSONObject jsonobj = new JSONObject();
        jsonobj.put("key", "你们好！这是websocket群体发送！" + System.currentTimeMillis());
        String text = JSONObject.toJSONString(jsonobj);
        webSocket.sendAllMessage(text);
        return text;
    }

    @GetMapping("/sendOneWebSocket/{userId}")
    public String sendOneWebSocket(@PathVariable("userId") String userId) {
//        String text = userId + " 你好！ 这是websocket单人发送！";
        JSONObject jsonobj = new JSONObject();
        jsonobj.put("key", "当前时间" + System.currentTimeMillis());
        String text = JSONObject.toJSONString(jsonobj);
        webSocket.sendOneMessage(userId, text);
        return text;
    }
}
