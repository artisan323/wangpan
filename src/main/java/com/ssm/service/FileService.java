package com.ssm.service;

import com.ssm.mapper.FileMapper;
import com.ssm.pojo.File;
import com.ssm.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Resource
    FileMapper fileMapper;

    //保存文件
    public void saveFile(File file){
        fileMapper.insFile(file);
    }

    //获取文件属性
    public int selFileTypeById(int id){
        return fileMapper.selFileTypeById(id);
    }

    //按照用户名查找所有文件
    public List<File> selFileByUsername(User user){
        List<File> list = fileMapper.selFileByUsername(user);
        return list;
    }

    //按照文件id查找文件,封装成list返回
    public List<File> getFileList(List<Integer> listId){
        List<File> list = new ArrayList<>();
        for (int i = 0; i < listId.size(); i++){
            list.add(fileMapper.selFileByFileId(listId.get(i)));
        }
        return list;
    }

    //按照文件名称下载文件
    public File selFileByFileId(int id){
        return fileMapper.selFileByFileId(id);
    }

    //判断文件类型
    public int getFiletype(String type){

        int fileType = 5;

        switch (type){
            //图片
            case ".png":
                fileType = 0;
                break;
            case ".jpg":
                fileType = 0;
                break;
            //文档
            case ".doc":
                fileType = 1;
                break;
            case ".docx":
                fileType = 1;
                break;
            //视频
            case ".mp4":
                fileType = 2;
                break;
            case ".avi":
                fileType = 2;
                System.out.println(fileType);
            //音乐
            case ".mp3":
                fileType = 3;
                break;
            //文本
            case ".txt":
                fileType = 4;
                break;
            //其他文件
            default:
                fileType = 5;
                break;
        }
        System.out.println(fileType);
        return fileType;

    }

    //创建文件夹
    public boolean mkdir(String dirname, String username){

        //文件路径
        String path = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile/";
        //String uuid = UUID.randomUUID().toString();

        //创建文件夹
        java.io.File file = new java.io.File(path + dirname);
        if(!file.exists()){//如果文件夹不存在
            file.mkdir();//创建文件夹

            //保存到数据库
            File dirFile = new File();
            dirFile.setUserName(username);
            dirFile.setFileType(6);
            dirFile.setFileName(dirname);
            dirFile.setFilePath(path);
            dirFile.setFileSize(file.length());
            dirFile.setSaveName(dirname);

            fileMapper.insFile(dirFile);
            return true;
        }

        return false;
    }

    //删除单个文件
    public void delFileById(int delids){

        String path = "/Users/wannengqingnian/MyCode/NetworkDiskSharing/src/main/webapp/uploadfile/";

        //本地删除
        String filename = fileMapper.selFileByFileId(delids).getSaveName();
        java.io.File file = new java.io.File(path+filename);
        if(file.exists()){
            file.delete();
        }

        //数据库删除
        fileMapper.delFileById(delids);
        System.out.println("delect " + delids + " success");
    }

    //删除多个文件
    public void delFileByIdList(String delids){

        String[] ids = delids.split("-");

        //获取所有下载的文件对象id
        List<Integer> listId = new ArrayList<>();
        for (int i = 0; i < ids.length; i++){
            listId.add(Integer.parseInt(ids[i]));
        }

        //循环在数据中删除
        for (int i = 0; i < listId.size(); i++){
            fileMapper.delFileById(listId.get(i));
        }
    }

    //文件重命名
    public void fileRename(int filleId, String fielNewName){
        File file = new File();
        file.setFileId(filleId);
        file.setFileName(fielNewName);
        fileMapper.upFileName(file);
    }
}
