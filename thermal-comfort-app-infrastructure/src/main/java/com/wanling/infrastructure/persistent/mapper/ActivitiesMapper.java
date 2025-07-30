package com.wanling.infrastructure.persistent.mapper;

import com.wanling.infrastructure.persistent.po.Activities;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivitiesMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Activities record);

    int insertSelective(Activities record);

    Activities selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Activities record);

    int updateByPrimaryKey(Activities record);

}
