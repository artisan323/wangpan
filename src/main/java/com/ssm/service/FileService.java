package com.ssm.service;

import com.ssm.mapper.FileMapper;
import com.ssm.pojo.File;
import com.ssm.pojo.User;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    @Resource
    FileMapper fileMapper;

    public void saveFile(File file){
        fileMapper.insFile(file);
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


}
