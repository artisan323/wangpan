package com.ssm.service;

import com.ssm.mapper.UserMapper;
import com.ssm.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    //通过用户名在数据库中查询是否有该用户
    public User findByName(String username){
        User selUser = userMapper.selUser(username);
        return selUser;
    }

    //判断数据库是否有该用户
    public boolean addUser(User user){
        if (userMapper.selUser(user.getUsername()) != null){
            return true;
        }
        return false;
    }

    public User findId(int i){
         User user = userMapper.selUserById(i);
         return user;
    }

    //注册时往数据库添加用户
    public void addNewNameSpace(User user){
        userMapper.addNewUser(user);
    }

    //往用户表里面添加文件大小
    public void addFileSize(User user, Long len){

        System.out.println(len + user.toString());

        String selSize = userMapper.selUser(user.getUsername()).getCountSize();

        //获取用户之前的文件大小
        if (selSize == null){
            user.setCountSize(String.valueOf(len));
            userMapper.addFIleLen(user);
        }else {

            //把之前的文件长度查询出来
            Long countSize = Long.valueOf(selSize);

            //两次相加
            String sumSize = String.valueOf(countSize+len);

            user.setCountSize(sumSize);
            System.out.println(sumSize);
            //保存到数据库
            userMapper.addFIleLen(user);
        }

    }
}
