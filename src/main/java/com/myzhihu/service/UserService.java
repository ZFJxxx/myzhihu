package com.myzhihu.service;

import com.myzhihu.dao.LoginTicketDAO;
import com.myzhihu.dao.UserDAO;
import com.myzhihu.pojo.LoginTicket;
import com.myzhihu.pojo.User;
import com.myzhihu.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    //注册
    public Map<String,String> regiseter(String username,String password){
        Map<String,String> map = new HashMap<String,String>();
        if(StringUtils.isBlank(username)){
             map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","用户名不能为空");
             return map;
        }
        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg","用户名自己被注册！");
            return map;
        }

        //用户注册，密码md5盐加密
        User addUser = new User();
        addUser.setName(username);
        addUser.setSalt(UUID.randomUUID().toString().substring(0,5) );
        addUser.setHeadUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4080609108,1550036849&fm=27&gp=0.jpg");
        addUser.setPassword(MD5Util.MD5(password+addUser.getSalt()));
        userDAO.addUser(addUser);

        //注册后记一个ticket ，登陆状态
        String ticket = addLoginTicket(addUser.getId());
        //返回给浏览器，通过HttpServletResponse下发
        map.put("ticket",ticket);
        return map;
    }

    //登陆
    public Map<String ,String> Login(String username,String password){
        Map<String,String> map = new HashMap<String ,String>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","用户名不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg","用户名不存在！");
            return map;
        }

        if(!MD5Util.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }
        //登陆后记一个ticket ，登陆状态
        String ticket = addLoginTicket(user.getId());
        //返回给浏览器，通过HttpServletResponse下发
        map.put("ticket",ticket);
        return map;
    }

    //增加ticket
    private String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600*24*100+now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
