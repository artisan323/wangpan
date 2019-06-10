package com.ssm.mapper;

import com.ssm.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("select * from user where username = #{0} ")
    User selUser(String username);

    @Select("select * from usera where id = 1")
    User selUserById(int i);

    @Insert("insert into user(username, password) values(#{username}, #{password})")
    void addNewUser(User user);
}
