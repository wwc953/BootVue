package com.sg.vue.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@Component
//此注解相当于设置访问URL
@ServerEndpoint("/websocket/{userId}")
public class WebSocketServer {

    private Session session;

    private static CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<>();
    private static Map<String, Session> sessionPool = new HashMap<String, Session>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        this.session = session;
        webSockets.add(this);
        sessionPool.put(userId, session);
        System.out.println(userId + "【websocket消息】有新的连接，总数为:" + webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        System.out.println("【websocket消息】连接断开，总数为:" + webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("【websocket消息】收到客户端消息:" + message);
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        for (WebSocketServer webSocket : webSockets) {
            System.out.println("【websocket消息】广播消息:" + message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String userId, String message) {
        System.out.println("【websocket消息】单点消息:" + message);
        Session session = sessionPool.get(userId);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
