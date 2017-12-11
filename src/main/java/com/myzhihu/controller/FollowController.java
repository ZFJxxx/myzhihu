package com.myzhihu.controller;

import com.myzhihu.async.EventModel;
import com.myzhihu.async.EventProducer;
import com.myzhihu.async.EventType;
import com.myzhihu.pojo.*;
import com.myzhihu.service.CommentService;
import com.myzhihu.service.FollowService;
import com.myzhihu.service.QuestionService;
import com.myzhihu.service.UserService;
import com.myzhihu.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {
    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    //关注用户
    @RequestMapping(path = {"/followUser"},method={RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null){
            return JSONUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        //关注之后发站内信通知
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        //返回我关注了多少人
        return JSONUtil.getJSONString(ret?0 :1 ,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }

    //取关用户
    @RequestMapping(path = {"/unfollowUser"},method={RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null){
            return JSONUtil.getJSONString(999);
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        //取消关注之后发出事件
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        //返回我关注了多少人
        return JSONUtil.getJSONString(ret?0 :1 ,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }

    //关注问题
    @RequestMapping(path = {"/followQuestion"},method={RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return JSONUtil.getJSONString(999);
        }
        Question question = questionService.getQuestionById(questionId);
        if(question == null){
            return JSONUtil.getJSONString(1,"问题不存在");
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);
        //关注之后发出事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(question.getUserId()));

        //回显
        Map<String,Object> info = new HashMap<String,Object>();
        info.put("headUrl",hostHolder.getUser().getHeadUrl());
        info.put("name",hostHolder.getUser().getName());
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));

        return JSONUtil.getJSONString(ret?0 :1 ,info);
    }

    //取消关注问题
    @RequestMapping(path = {"/unfollowQuestion"},method={RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return JSONUtil.getJSONString(999);
        }
        Question question = questionService.getQuestionById(questionId);
        if(question == null){
            return JSONUtil.getJSONString(1,"问题不存在");
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);
        //取消关注之后发出事件
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(question.getUserId()));

        Map<String, Object> info = new HashMap<>();
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        return JSONUtil.getJSONString(ret?0 :1 ,info);
    }

    //我所有的粉丝
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    //我关注的所有人
    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);

        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.put("user", user);
            vo.put("commentCount", commentService.getUserCommentCount(uid));
            vo.put("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.put("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.put("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.put("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
