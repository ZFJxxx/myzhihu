package com.myzhihu.controller;

import com.myzhihu.async.EventModel;
import com.myzhihu.async.EventProducer;
import com.myzhihu.async.EventType;
import com.myzhihu.pojo.Comment;
import com.myzhihu.pojo.EntityType;
import com.myzhihu.pojo.HostHolder;
import com.myzhihu.service.CommentService;
import com.myzhihu.service.LikeService;
import com.myzhihu.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path=("/like"),method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser() == null){
            return JSONUtil.getJSONString(999);
        }

        //异步队列，发布一个点赞的Event出去
        Comment comment =commentService.getCommentById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(),commentId, EntityType.ENTITY_COMMENT);
        return JSONUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path=("/dislike"),method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId")int commentId){
        if(hostHolder.getUser() == null){
            return JSONUtil.getJSONString(999);
        }

        long likeCount = likeService.disLike(hostHolder.getUser().getId(),commentId, EntityType.ENTITY_COMMENT);
        return JSONUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
