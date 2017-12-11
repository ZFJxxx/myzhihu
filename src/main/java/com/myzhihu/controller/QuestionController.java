package com.myzhihu.controller;


import com.myzhihu.pojo.*;
import com.myzhihu.service.CommentService;
import com.myzhihu.service.LikeService;
import com.myzhihu.service.QuestionService;
import com.myzhihu.service.UserService;
import com.myzhihu.util.JSONUtil;
import com.myzhihu.util.myzhihuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
   private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

   @Autowired
   UserService userService;
   @Autowired
   QuestionService questionService;
   @Autowired
   HostHolder hostHolder;
   @Autowired
   CommentService commentService;
   @Autowired
   LikeService likeService;

   @RequestMapping(value="/question/add",method = {RequestMethod.POST})
   @ResponseBody
   public String addQuestion(@RequestParam("title")String title,
                             @RequestParam("content")String content) {
       try{
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            if(hostHolder == null){
                return JSONUtil.getJSONString(999);
            }else {
                question.setUserId(hostHolder.getUser().getId());
            }

            if(questionService.addQuestion(question)>0){
                return JSONUtil.getJSONString(0);
            }
       }catch(Exception e){
           logger.error("添加题目失败"+e.getMessage());
       }
       return JSONUtil.getJSONString(1,"失败");
   }

   @RequestMapping(value=("/question/{questionId}"),method = {RequestMethod.GET})
   public String question(@PathVariable("questionId") int questionId,
                          Model model){
       Question question = questionService.getQuestionById(questionId);
       User user = userService.getUser(question.getUserId());
       model.addAttribute("question",question);
       model.addAttribute("user",user);

       List<Comment> commentList = commentService.getCommentsByEntity(questionId, EntityType.ENTITY_QUESTION);
       List<ViewObject> comments = new ArrayList<ViewObject>();
       for(Comment comment: commentList){
           ViewObject vo = new ViewObject();
           vo.put("comment",comment);
           if(hostHolder.getUser()==null){
               vo.put("liked",0);
           }else{
               vo.put("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),comment.getId(),EntityType.ENTITY_COMMENT));
           }
           vo.put("likeCount",likeService.getLikeCount(comment.getId(),EntityType.ENTITY_COMMENT));
           vo.put("user",userService.getUser(comment.getUserId()));
           comments.add(vo);
       }
       model.addAttribute("comments",comments);
       return "detail";
   }
}