package com.myzhihu.controller;

import com.myzhihu.async.EventModel;
import com.myzhihu.async.EventProducer;
import com.myzhihu.async.EventType;
import com.myzhihu.service.LoginTicketService;
import com.myzhihu.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class LoginController  {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    LoginTicketService loginTicketService;

    @Autowired
    EventProducer eventProducer;

    //登陆
    @RequestMapping(path={"/login/"},method={RequestMethod.POST})
    public String login(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("next") String next,
                      @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response){

        try {
            Map<String, Object> map = userService.Login(username,password);
            //存在ticket就创建cookie下发，反回主页
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                //没有ticket就看错在哪，返回登陆页面
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登录异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path={"/reg/"},method={RequestMethod.POST})
    public String regiseter(Model model,
                            @RequestParam("username")String username,
                            @RequestParam("password")String password,
                            @RequestParam("rememberme")boolean rememberme,
                            @RequestParam(value="next",required = false)String next,
                            HttpServletResponse response){
        try{
            Map<String,String > map = userService.regiseter(username,password);
            //存在ticket就创建cookie下发，反回主页
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/index";
            }else{
                //没有ticket就看错在哪，返回登陆页面
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch(Exception e ){
            logger.error("注册异常"+e.getMessage());
            model.addAttribute("msg","服务器错误");
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
