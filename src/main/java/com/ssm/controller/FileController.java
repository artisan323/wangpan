package com.ssm.controller;

import com.ssm.pojo.File;
import com.ssm.pojo.Msg;
import com.ssm.pojo.User;
import com.ssm.service.FileService;
import com.ssm.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class FileController {

    @Resource
    FileService fileService;

    @Resource
    UserService userService;

    //处理上传文件
    @RequestMapping("/file")
    public String file(MultipartFile uploadFile, HttpSession session) throws IOException {

        //创建文件对象，保存信息
        File file = new File();

        //获得文件名称
        String upfileName = uploadFile.getOriginalFilename();
        System.out.println(upfileName);

        //手动设置文件保存路径
        String upPath = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile";

        //通过随机数来设置文件保存时名称
        String suffix = upfileName.substring(upfileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();

        //保存文件到本地
        FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), new java.io.File(upPath+uuid+suffix));

        //得到用户对象
        User user = (User) session.getAttribute("user");

        //获取文件大小
        Long len = uploadFile.getSize();

        //保存到file表中
        file.setUserName(user.getUsername());
        file.setFilePath(upPath);
        file.setFileName(upfileName);
        file.setFileSize(len);
        fileService.saveFile(file);

        System.out.println(len);

        //把文件大小保存到用户表中
        userService.addFileSize(user, len);

        return "menu";

    }

    //根据f类型查询文件
    @RequestMapping("/search")
    @ResponseBody
    public Msg getAllFile(@RequestParam(value = "f", defaultValue = "1") int f, HttpSession session){

        //得到用户对象
        User user = (User) session.getAttribute("user");

        List<File> list = fileService.selFileByUsername(user);

        return Msg.success().add("list", list);
    }
}
