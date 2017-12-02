package com.myzhihu.pojo;

import org.springframework.stereotype.Component;

@Component
//当前访问页面的用户(ticket关联的userId)
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    //获取当前线程保存的user
    public User getUser(){
        return users.get();
    }

    //存入
    public void setUser(User user){
        users.set(user);
    }

    //删除
    public void clear(){
        users.remove();
    }
}
