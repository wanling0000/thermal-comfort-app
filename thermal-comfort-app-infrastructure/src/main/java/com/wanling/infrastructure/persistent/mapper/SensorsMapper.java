package com.wanling.infrastructure.persistent.mapper;

import com.wanling.infrastructure.persistent.po.Sensors;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SensorsMapper {

    int deleteByPrimaryKey(String id);

    int insert(Sensors record);

    int insertSelective(Sensors record);

    Sensors selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Sensors record);

    int updateByPrimaryKey(Sensors record);

}
