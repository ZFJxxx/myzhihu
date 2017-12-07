package com.myzhihu.service;

import com.myzhihu.dao.MessageDAO;
import com.myzhihu.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message)>0? message.getId():0;
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit ){
        return messageDAO.getConversationIdDetail(conversationId,offset,limit);
    }
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConvesationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConvesationUnreadCount(userId, conversationId);
    }
}
