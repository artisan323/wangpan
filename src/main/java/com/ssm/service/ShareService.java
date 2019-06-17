package com.ssm.service;

import com.ssm.mapper.ShareMapper;
import com.ssm.pojo.Share;
import com.ssm.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShareService {

    @Resource
    ShareMapper shareMapper;

    public List<Share> selAllShare(User user){
        return shareMapper.selShare(user);
    }
}
