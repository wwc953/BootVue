package com.sg.vue.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @Description:  redis订阅
 * @author: wangwc
 * @date: 2020/9/8 14:02
 */
@Configuration
public class SubConfig {

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(listenerAdapter, new PatternTopic("TextChannel"));
        container.addMessageListener(listenerAdapter, webMsgChannelTopic());
        return container;
    }

    @Bean
    public ChannelTopic webMsgChannelTopic(){
        return new ChannelTopic("webMsg");
    }

}
