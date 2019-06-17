package com.ssm.controller;

import com.ssm.pojo.Msg;
import com.ssm.pojo.Share;
import com.ssm.pojo.User;
import com.ssm.service.ShareService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShareController {

    @Resource
    ShareService selShare;

    @RequestMapping("/sharelist")
    @ResponseBody
    public Msg sharelist(HttpSession session){

        User user = (User) session.getAttribute("user");
        List<Share> list = selShare.selAllShare(user);

        return Msg.success().add("list", list);
    }

}
