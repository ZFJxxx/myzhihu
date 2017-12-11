package com.myzhihu.async.handler;

import com.myzhihu.async.EventHandler;
import com.myzhihu.async.EventModel;
import com.myzhihu.async.EventType;
import com.myzhihu.pojo.Message;
import com.myzhihu.pojo.User;
import com.myzhihu.service.MessageService;
import com.myzhihu.service.UserService;
import com.myzhihu.util.myzhihuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromUserId(myzhihuUtil.SYSTEM_USERID);
        message.setToUserId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt("questionId"));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
