package com.ssm.mapper;

import com.ssm.pojo.Share;

import com.ssm.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ShareMapper {

    @Insert("insert into share(shareUrl, shareUser, status, shareFileName) values(#{shareUrl}, #{shareUser}, #{status}, #{shareFileName})")
    void saveShare(Share share);


    @Select("select * from share where shareUser = #{username}")
    List<Share> selShare(User user);

}
