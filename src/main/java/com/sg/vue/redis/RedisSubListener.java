package com.sg.vue.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * Redis消息监听
 */
public class RedisSubListener extends MessageListenerAdapter {

    @Override
    public void onMessage(Message message, byte[] pattern) {
//        String body = new String(message.getBody());
//        String channel = new String(message.getChannel());
//        System.out.println("message.getBody:" + body + "message.getChannel:" + channel + "，pattern：" + new String(pattern));
        System.out.println("---2222---message:" + message+ "，pattern：" + new String(pattern));
    }

}
