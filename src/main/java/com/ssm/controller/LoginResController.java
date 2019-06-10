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
    UserService service;

    @RequestMapping("login")
    public String login(HttpServletRequest request, String username, String password){

        System.out.println(username + password);
        User isUser =  service.findByName(username);

        if (username == null || password == null){
            request.setAttribute("msg", "请输入用户名密码qqq");
            return "error";
        }else if (isUser == null){
            request.setAttribute("msg", "用户名密码不匹配qqq");
            return "error";
        }else if (isUser != null){
            HttpSession session = request.getSession();
            session.setAttribute("user", isUser);
            return "success";
        }
        return "error";
    }

}
