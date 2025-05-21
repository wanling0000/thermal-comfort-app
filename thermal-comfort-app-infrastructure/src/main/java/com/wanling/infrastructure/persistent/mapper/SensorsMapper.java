package com.wanling.infrastructure.persistent.mapper;

import com.wanling.infrastructure.persistent.po.Sensors;

/**
* @author fwl
* @description 针对表【sensors】的数据库操作Mapper
* @createDate 2025-05-05 22:18:16
* @Entity com.wanling.infrastructure.persistent.po.Sensors
*/
public interface SensorsMapper {

    int deleteByPrimaryKey(String id);

    int insert(Sensors record);

    int insertSelective(Sensors record);

    Sensors selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Sensors record);

    int updateByPrimaryKey(Sensors record);

}
