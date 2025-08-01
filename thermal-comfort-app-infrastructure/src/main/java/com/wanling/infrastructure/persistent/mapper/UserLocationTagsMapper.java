package com.wanling.infrastructure.persistent.mapper;

import java.util.List;

import com.wanling.infrastructure.persistent.po.UserLocationTags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author fwl
* @description 针对表【user_location_tags】的数据库操作Mapper
* @createDate 2025-05-19 22:50:01
* @Entity com.wanling.infrastructure.persistent.po.UserLocationTags
*/

@Mapper
public interface UserLocationTagsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserLocationTags record);

    int insertSelective(UserLocationTags record);

    UserLocationTags selectByPrimaryKey(String userLocationTagId);

    int updateByPrimaryKeySelective(UserLocationTags record);

    int updateByPrimaryKey(UserLocationTags record);

    UserLocationTags selectByUserAndName(
            @Param("userId") String userId,
            @Param("tagName") String tagName
    );

    UserLocationTags findById(String userLocationTagId);

    List<UserLocationTags> findByUserId(String userId);
}
