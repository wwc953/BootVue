package com.sg.vue.redis;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sg.vue.utils.JsonUtil;
import com.sg.vue.websocket.SendDTO;
import com.sg.vue.websocket.WebSocketServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.UnsupportedEncodingException;

/**
 * Redis消息监听
 */
@Slf4j
public class WebMsgListener extends MessageListenerAdapter {

    @Resource
    WebSocketServer webSocketServer;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("------message:" + message + "，pattern：" + new String(pattern));
        String msgString = new String(message.getBody());
//        SendDTO sendDTO = JSONObject.parseObject(msgString, SendDTO.class);
        SendDTO sendDTO = JsonUtil.convertJsonToObject(msgString, new TypeReference<SendDTO>() {
        });
        webSocketServer.sendOneMessage(sendDTO.getReceiver(), JsonUtil.convertObjectToJson(sendDTO));
    }

}
