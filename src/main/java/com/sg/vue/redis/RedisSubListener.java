package com.sg.vue.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * Redis消息监听
 */
@Component
public class RedisSubListener extends MessageListenerAdapter {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("msg:" + message + "，pattern：" + new String(pattern));
    }
}
