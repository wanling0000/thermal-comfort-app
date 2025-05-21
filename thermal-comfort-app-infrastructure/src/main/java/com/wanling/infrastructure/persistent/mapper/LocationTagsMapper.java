package com.wanling.infrastructure.persistent.mapper;

import com.wanling.infrastructure.persistent.po.LocationTags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author fwl
* @description 针对表【location_tags】的数据库操作Mapper
* @createDate 2025-05-19 22:48:00
* @Entity com.wanling.infrastructure.persistent.po.LocationTags
*/
@Mapper
public interface LocationTagsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(LocationTags record);

    int insertSelective(LocationTags record);

    LocationTags selectByPrimaryKey(@Param("location_tag_id") String id);

    int updateByPrimaryKeySelective(LocationTags record);

    int updateByPrimaryKey(LocationTags record);

    LocationTags findNearbyWithSameName(@Param("displayName") String displayName,
                                         @Param("latitude") Double lat,
                                         @Param("longitude") Double lng);

    int insertLocationTag(LocationTags po);

}
