package com.myzhihu.controller;

import com.myzhihu.pojo.HostHolder;
import com.myzhihu.pojo.Message;
import com.myzhihu.pojo.User;
import com.myzhihu.pojo.ViewObject;
import com.myzhihu.service.MessageService;
import com.myzhihu.service.UserService;
import com.myzhihu.util.JSONUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @RequestMapping(path ={"/msg/addMessage"},method={RequestMethod.POST})
    @ResponseBody
    public String addMessage(@Param("toName")String toName,
                             @Param("content")String content){
        try{
            if(hostHolder.getUser() == null){
                return JSONUtil.getJSONString(999,"未登录");
            }

            User user = userService.selectUserByName(toName);
            if(user == null){
                return JSONUtil.getJSONString(1,"用户不存在");
            }

            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setFromUserId(hostHolder.getUser().getId());
            message.setToUserId(user.getId());
            message.setContent(content);
            messageService.addMessage(message);

            return JSONUtil.getJSONString(0);
        }catch(Exception e){
            logger.error("发送消息失败"+e.getMessage());
            return JSONUtil.getJSONString(1,"发信失败");
        }
    }

    @RequestMapping(path ={"/msg/detail"},method={RequestMethod.GET})
    public String getConversationDetail(Model model, @RequestParam("conversationId")String conversationId){
        try{
            List<Message> messageList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for(Message message: messageList){
                ViewObject vo = new ViewObject();
                vo.put("message",message);
                vo.put("user",userService.getUser(message.getFromUserId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch(Exception e){
            logger.error("获取详情失败"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path ={"/msg/list"},method={RequestMethod.GET})
    public String getConversationList(Model model){
        if(hostHolder == null){
            return "redirect:/reglogin";
        }
        User localUser = hostHolder.getUser();
        List<Message> conversationList = messageService.getConversationList(localUser.getId(),0,10);
        List<ViewObject> conversations = new ArrayList<ViewObject>();
        for(Message message : conversationList){
            ViewObject vo = new ViewObject();
            vo.put("conversation",message);
            int targetId = message.getFromUserId() == localUser.getId() ? message.getToUserId():message.getFromUserId();
            vo.put("user",userService.getUser(targetId));
            vo.put("unread",messageService.getConvesationUnreadCount(localUser.getId(),message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute(conversations);
        return "letter";
    }
}
