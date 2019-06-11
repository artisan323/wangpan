package com.ssm.service;

import com.ssm.mapper.FileMapper;
import com.ssm.pojo.File;
import com.ssm.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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



}
