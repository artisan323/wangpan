package com.ssm.controller;

import com.ssm.pojo.File;
import com.ssm.pojo.User;
import com.ssm.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Controller
public class FileController {

    @Resource
    FileService fileService;

    //处理上传文件
    @RequestMapping("/file")
    public String file(MultipartFile uploadFile, HttpSession session) throws IOException {

            //创建文件对象，保存信息
            File file = new File();

            String upfileName = uploadFile.getOriginalFilename();
            System.out.println(upfileName);
            String upPath = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile";
            String suffix = upfileName.substring(upfileName.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();
            FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), new java.io.File(upPath+uuid+suffix));

            User user = (User) session.getAttribute("user");
            file.setUserName(user.getUsername());
            file.setFilePath(upPath);

            fileService.saveFile(file);
            return "show";

    }

}
