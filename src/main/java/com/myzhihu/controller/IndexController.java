package com.myzhihu.controller;

import com.myzhihu.pojo.EntityType;
import com.myzhihu.pojo.Question;
import com.myzhihu.pojo.ViewObject;
import com.myzhihu.service.FollowService;
import com.myzhihu.service.QuestionService;
import com.myzhihu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @RequestMapping(path = {"/","/index"} ,method ={RequestMethod.GET})
    public String index(Model model) {
        model.addAttribute("vos",getQuestions(0,0,10));
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"} ,method ={RequestMethod.GET})
    public String userIndex(Model model,
                            @PathVariable("userId") int userId) {
        model.addAttribute("vos",getQuestions(userId,0,10));
        return "index";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.put("question", question);
            vo.put("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));
            vo.put("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
