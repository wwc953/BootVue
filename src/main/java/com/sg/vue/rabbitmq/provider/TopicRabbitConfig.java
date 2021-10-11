//package com.sg.vue.rabbitmq.provider;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TopicRabbitConfig {
//    //绑定键
//    public final static String webmsg = "topic.webmsg";
//
//    @Bean
//    public Queue webmsgQueue() {
//        return new Queue(webmsg);
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("rabbittopicExchange");
//    }
//
//    //将webmsgQueue和topicExchange绑定,而且绑定的键值为topic.webmsg
//    //这样只要是消息携带的路由键是topic.webmsg,才会分发到该队列
//    @Bean
//    Binding bindingExchangeMessage() {
//        return BindingBuilder.bind(webmsgQueue()).to(exchange()).with(webmsg);
//    }
//
//}
