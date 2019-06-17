package com.ssm.mapper;

import com.ssm.pojo.File;
import com.ssm.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface FileMapper {

    @Insert("insert into file(userName, filePath, fileName, fileSize, fileType, saveName) values(#{userName}, #{filePath}, #{fileName}, #{fileSize}, #{fileType}, #{saveName})")
    void insFile(File file);

    @Select("select * from file where userName = #{username}")
    List<File> selFileByUsername(User user);

    @Select("select * from file where fileId = #{id}")
    File selFileByFileId(int id);

    @Delete("delete from file where fileId = #{id}")
    void delFileById(int id);

    @Select("select fileType from file where fileId = #{id}")
    int selFileTypeById(int id);

    @Update("update file set fileName = #{fileName} where fileId = #{fileId}")
    void upFileName(File file);
}
