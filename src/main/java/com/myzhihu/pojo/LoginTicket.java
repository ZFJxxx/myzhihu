package com.myzhihu.pojo;

import java.util.Date;

//服务器端token关联userId,记住登陆状态
public class LoginTicket {
    private int id;
    private int userId;
    private Date expired;  //过期时间
    private int status;    //登陆状态 0代表有效，1代表无效
    private String ticket;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }



}
