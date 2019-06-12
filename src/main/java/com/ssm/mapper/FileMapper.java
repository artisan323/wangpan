package com.ssm.mapper;

import com.ssm.pojo.File;
import com.ssm.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FileMapper {

    @Insert("insert into file(userName, filePath, fileName, fileSize, saveName) values(#{userName}, #{filePath}, #{fileName}, #{fileSize}, #{saveName})")
    void insFile(File file);

    @Select("select * from file where userName = #{username}")
    List<File> selFileByUsername(User user);

    @Select("select * from file where fileId = #{id}")
    File selFileByFileId(int id);
}
