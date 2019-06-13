package com.ssm.controller;

import com.ssm.pojo.File;
import com.ssm.pojo.Msg;
import com.ssm.pojo.User;
import com.ssm.service.FileService;
import com.ssm.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


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

        //手动设置文件保存路径
        String upPath = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile/";

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
        file.setSaveName(uuid+suffix);
        file.setFileType(fileService.getFiletype(suffix));
        System.out.println("fileService.getFiletype(suffix) = " + fileService.getFiletype(suffix));
        fileService.saveFile(file);

        //把文件大小保存到用户表中
        userService.addFileSize(user, len);

        return "menu";

    }

    //根据f类型查询文件
    @RequestMapping("/search")
    @ResponseBody
    public Msg getAllFile(@RequestParam(value = "f", defaultValue = "5") int f, HttpSession session){

        //得到用户对象
        User user = (User) session.getAttribute("user");
        //查询出所有该用户保存的文件
        List<File> list = fileService.selFileByUsername(user);

        //返回所有文件
        if (f == 6){
            return Msg.success().add("list", list);
        }

        //在所有文件中找出所有符合文件类型的文件
        List<File> typeList = new ArrayList<>();
        for (File item : list) {
            if (item.getFileType() == f){
                typeList.add(item);
            }
        }

        //如果符合类型文件不存在，返回失败
        if (typeList.size() == 0){
            return  Msg.fail().add("list", typeList);
        }
        System.out.println("中文");

        return Msg.success().add("list", typeList);
    }

    //下载文件请求
    @RequestMapping("/down")
    @ResponseBody
    public void down(int fileId, HttpServletResponse res, HttpServletRequest req) throws IOException {

        //得到文件名称
        File myfile = fileService.selFileByFileId(fileId);
        String filename = myfile.getSaveName();
        System.out.println(myfile);

        res.setHeader("Content-Disposition", "attachment;filename=" + filename);
        ServletOutputStream so = res.getOutputStream();
        String path = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile";
        java.io.File file = new java.io.File(path, filename);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        so.write(bytes);
        so.flush();
        so.close();
        System.out.println("文件下载" + fileId);
    }

    //多文件下载
    @RequestMapping(value = "/downs")
    @ResponseBody
    public void downs(String downids, HttpServletResponse response, HttpServletRequest request) throws IOException {

        System.out.println("downids = "+downids);

        //封装所有id值
        String[] ids = downids.split("-");

        //获取所有下载的文件对象id
        List<Integer> listId = new ArrayList<>();
        for (int i = 0; i < ids.length; i++){
            listId.add(Integer.parseInt(ids[i]));
        }

        //获取所有下载的文件对象id
        List<File> fileList = fileService.getFileList(listId);//查询数据库中记录

        String path = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile/";


        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");

        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        String downloadName = "files.zip";
        String agent = request.getHeader("USER-AGENT");
        try {
            if (agent.contains("MSIE")||agent.contains("Trident")) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                downloadName = new String(downloadName.getBytes("UTF-8"),"ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");

        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
        } catch (Exception e) {
            e.printStackTrace();
        }

        //循环将文件写入压缩流
        DataOutputStream os = null;
        int size = fileList.size();
        for(int i = 0; i < size; i++ ){

            File myfile = fileList.get(i);

            java.io.File file = new java.io.File(path+myfile.getSaveName());
            try {
                //添加ZipEntry，并ZipEntry中写入文件流
                //这里，加上i是防止要下载的文件有重名的导致下载失败
                zipos.putNextEntry(new ZipEntry(i + myfile.getSaveName()));
                os = new DataOutputStream(zipos);
                InputStream is = new FileInputStream(file);
                byte[] b = new byte[100];
                int length = 0;
                while((length = is.read(b))!= -1){
                    os.write(b, 0, length);
                }
                is.close();
                zipos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //关闭流
        try {
            os.flush();
            os.close();
            zipos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
