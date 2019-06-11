package com.ssm.mapper;

import com.ssm.pojo.File;
import org.apache.ibatis.annotations.Insert;

public interface FileMapper {

    @Insert("insert into file(userName, filePath) values(#{userName}, #{filePath})")
    void insFile(File file);

}
