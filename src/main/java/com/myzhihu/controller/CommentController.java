package com.myzhihu.controller;

import com.myzhihu.pojo.Comment;
import com.myzhihu.pojo.EntityType;
import com.myzhihu.pojo.HostHolder;
import com.myzhihu.service.CommentService;
import com.myzhihu.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path={"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId")int questionId ,
                             @RequestParam("content")String content){
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                return "redirect:/reglogin";
            }
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setStatus(0);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(questionId,EntityType.ENTITY_QUESTION);
            questionService.updateCommentCount(questionId,count);
        }catch (Exception e){
            logger.error("增加评论失败" + e.getMessage());
        }
        return "redirect:/question/" +questionId;
    }
}
