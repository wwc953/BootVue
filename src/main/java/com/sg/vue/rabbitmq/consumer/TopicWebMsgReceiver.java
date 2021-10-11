//package com.sg.vue.rabbitmq.consumer;
//
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//@RabbitListener(queues = "topic.webmsg")
//public class TopicWebMsgReceiver {
//
//    @RabbitHandler
//    public void process(Map webMsg) {
//        System.out.println("webMsg消费者收到消息  : " + webMsg.toString());
//    }
//}
