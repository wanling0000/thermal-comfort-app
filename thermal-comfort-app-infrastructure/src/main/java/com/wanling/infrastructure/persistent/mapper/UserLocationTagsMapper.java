package com.wanling.infrastructure.persistent.mapper;

import com.wanling.infrastructure.persistent.po.UserLocationTags;
import org.apache.ibatis.annotations.Mapper;

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

    UserLocationTags selectByUserAndName(String userId, String tagName);
}
