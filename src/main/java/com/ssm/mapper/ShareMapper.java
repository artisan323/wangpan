package com.ssm.mapper;

import com.ssm.pojo.Share;
import org.apache.ibatis.annotations.Insert;

public interface ShareMapper {

    @Insert("insert into share(shareUrl, shareUser, status) values(#{shareUrl}, #{shareUser}, #{status})")
    public void saveShare(Share share);
}
