package com.ssm.controller;

import com.ssm.pojo.User;
import com.ssm.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginResController {

    @Resource
    UserService userservice;

    @RequestMapping("login")
    public String login(HttpServletRequest request, String username, String password){

        System.out.println(username + password);
        User isUser =  userservice.findByName(username);

        if (username == null || password == null){
            request.setAttribute("msg", "请输入用户名密码");
            return "redirect:/index.jsp";
        }else if (isUser == null){
            request.setAttribute("msg", "用户名密码不匹配");
            return "redirect:/index.jsp";
        }else if (isUser != null){
            HttpSession session = request.getSession();
            session.setAttribute("user", isUser);
            return "menu";
        }
        return "redirect:/index.jsp";
    }

    @RequestMapping("register")
    public String register(){
        return "register";
    }

    @RequestMapping("register2")
    public String register2(HttpServletRequest request,User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return "register";
        } else {
            System.out.println(user.toString());
            //判断数据库是不是有该用户
            boolean isSuccess = userservice.addUser(user);
            System.out.println(isSuccess);
            if (isSuccess == false) {
                //往数据库添加新用户
                userservice.addNewNameSpace(user);
                //返回登陆页面
                return "redirect:/menu.jsp";
            } else {
                request.setAttribute("msg", "注册失败");
                return "register";
            }
        }
    }
}
