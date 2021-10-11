//package com.sg.vue.rabbitmq.provider;
//
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//
//@RestController
//@RequestMapping("/rabbit")
//public class SendMessageController {
//
//    @Autowired
//    RabbitTemplate rabbitTemplate;
//
//    @GetMapping("/sendTopicMessage")
//    public String sendTopicMessage() {
//        String messageId = String.valueOf(UUID.randomUUID());
//        String messageData = "message: 阿斯顿撒多 ";
//        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        Map<String, Object> manMap = new HashMap<>();
//        manMap.put("messageId", messageId);
//        manMap.put("messageData", messageData);
//        manMap.put("createTime", createTime);
//        rabbitTemplate.convertAndSend("rabbittopicExchange", TopicRabbitConfig.webmsg, manMap);
//        return "ok";
//    }
//
//
//}
