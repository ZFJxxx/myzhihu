package com.myzhihu.pojo;

import java.util.Date;

public class Message {
    private int id;
    private int fromUserId;
    private int toUserId;
    private String content;
    private Date createdDate;
    private int hasRead;
    private String conversationId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getConversationId() {
        if(fromUserId < toUserId){
            return String.format("%d_%d",fromUserId,toUserId);
        }else{
            return String.format("%d_%d",toUserId,fromUserId);
        }
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
