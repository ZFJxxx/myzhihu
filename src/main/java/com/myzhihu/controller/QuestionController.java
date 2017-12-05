package com.myzhihu.controller;


import com.myzhihu.pojo.HostHolder;
import com.myzhihu.pojo.Question;
import com.myzhihu.pojo.User;
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

import java.util.Date;

@Controller
public class QuestionController {
   private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

   @Autowired
   UserService userService;
   @Autowired
   QuestionService questionService;
   @Autowired
   HostHolder hostHolder;

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
                question.setUserId(999);
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
       return "detail";
   }
}