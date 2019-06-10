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

    public User findId(int i){
         User user = userMapper.selUserById(i);
         return user;
    }
}
