package com.ssm.service;

import com.ssm.mapper.FileMapper;
import com.ssm.pojo.File;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileService {

    @Resource
    FileMapper fileMapper;

    public void saveFile(File file){
        fileMapper.insFile(file);
    }



}
