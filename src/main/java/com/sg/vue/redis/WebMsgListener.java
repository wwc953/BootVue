package com.sg.vue.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sg.vue.utils.JsonUtil;
import com.sg.vue.websocket.SendDTO;
import com.sg.vue.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import javax.annotation.Resource;

/**
 * Redis消息监听
 */
@Slf4j
public class WebMsgListener extends MessageListenerAdapter {

    @Resource
    WebSocketServer webSocketServer;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msgString = new String(message.getBody());
        SendDTO sendDTO = JsonUtil.convertJsonToObject(msgString, new TypeReference<SendDTO>() {
        });
        if ("topic".equalsIgnoreCase(sendDTO.getSendType())) {
            webSocketServer.sendAllMessage(JsonUtil.convertObjectToJson(sendDTO));
        } else {
            webSocketServer.sendOneMessage(sendDTO.getReceiver(), JsonUtil.convertObjectToJson(sendDTO));
        }

    }

}
