package com.myzhihu.service;

import com.myzhihu.dao.LoginTicketDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginTicketService {
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public int getUserId(String ticket){
        return loginTicketDAO.selectUserIdByTicket(ticket);
    }

}
