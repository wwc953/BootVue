package com.sg.vue.websocket;

import java.io.Serializable;

public class SendDTO  implements Serializable {
    //接收人
    private String receiver;
    //发送人
    private String sender;
    private String senderName;
    private String title;
    private String msgID;
    private String msgContent;
    private String sendTime;
    private String sendRecID;
    //发送类型: 单点，广播
    private String sendType;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendRecID() {
        return sendRecID;
    }

    public void setSendRecID(String sendRecID) {
        this.sendRecID = sendRecID;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
}
