package com.ssm.mapper;

import com.ssm.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {

    @Select("select * from user where username = #{0} ")
    User selUser(String username);

    @Select("select * from user where id = 1")
    User selUserById(int i);

    @Insert("insert into user(username, password) values(#{username}, #{password})")
    void addNewUser(User user);

    //更新用户表文件大小操作
    @Update("update user set countSize = #{countSize} where username = #{username}")
    void addFIleLen(User user);
}
