package com.ssm.controller;

import com.ssm.pojo.File;
import com.ssm.pojo.Msg;
import com.ssm.pojo.Share;
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
import static com.ssm.util.FileSizeUtil.getNetFileSizeDescription;


@Controller
public class FileController {

    @Resource
    FileService fileService;

    @Resource
    UserService userService;

    //上传文件路径
    private final String LINUX_PATH = "/home/upFile/";
    private final String PC_PATH = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile/";
    //下载路径
    private final String DOWN_PATH = "/home/upFile";


    //处理上传文件
    @RequestMapping("/file")
    public String file(MultipartFile uploadFile, HttpSession session) throws IOException {

        //创建文件对象，保存信息
        File file = new File();

        //获得文件名称
        String upfileName = uploadFile.getOriginalFilename();

        //手动设置文件保存路径
//        String upPath = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile/";
        String upPath = LINUX_PATH;

        //通过随机数来设置文件保存时名称
        String suffix = upfileName.substring(upfileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();

        //保存文件到本地 文件工具类的方法保存上传的文件到本地
        FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), new java.io.File(upPath+uuid+suffix));

        //得到用户对象
        User user = (User) session.getAttribute("user");

        //获取文件大小
        Long len = uploadFile.getSize();
        String size = getNetFileSizeDescription(len);

        //保存到file表中   把上传文件的各个属性保存成一个一个文件对象
        file.setUserName(user.getUsername());
        file.setFilePath(upPath);
        file.setFileName(upfileName);
        file.setFileSize(size);
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
        System.out.printf(user.toString());
        List<File> list = fileService.selFileByUsername(user);
        System.out.printf(list.toString());

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
//        String path = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile";
        String path = DOWN_PATH;
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

//        String path = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile/";
        String path = DOWN_PATH;


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

    //单文件删除和多文件删除
    @RequestMapping("/del")
    @ResponseBody
    public Msg del(String delids){


        if (delids.contains("-") == false){
            //删除单文件
            int id = Integer.parseInt(delids);
            fileService.delFileById(id);
        }else {
            //删除多文件
            fileService.delFileByIdList(delids);
        }
        return Msg.success().add("success", "删除成功");
    }

    //创建文件夹
    @RequestMapping("/mkdir")
    public String mkdir(String dirname, HttpSession session){
        User user = (User) session.getAttribute("user");
        fileService.mkdir(dirname, user.getUsername());
        return "menu";
    }

    //文件重命名
    @RequestMapping("/rename")
    @ResponseBody
    public String rename(int renameId, String fileNewName){
        fileService.fileRename(renameId, fileNewName);
        System.out.println(renameId + fileNewName);
        return "menu";
    }

    //按照文件名查询文件
    @RequestMapping("/selfile")
    @ResponseBody
    public Msg selfile(String selName){

        List<File> list = fileService.selFileByFileName(selName);

        if (list.size() == 0){
            return Msg.fail();
        }
        return Msg.success().add("list", list);
    }

    //文件分享
    @RequestMapping("/share")
    @ResponseBody
    public Msg share(int shareId){
        Share share = fileService.share(shareId);
        if (share == null){
            return Msg.fail();
        }
        return Msg.success().add("share", share);
    }


}
