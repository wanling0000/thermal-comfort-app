package com.wanling.infrastructure.persistent.mapper;

import java.util.UUID;

import com.wanling.infrastructure.persistent.po.EnvironmentalReadings;
import org.apache.ibatis.annotations.Mapper;

/**
* @author fwl
* @description 针对表【environmental_readings】的数据库操作Mapper
* @createDate 2025-05-05 22:18:16
* @Entity com.wanling.infrastructure.persistent.po.EnvironmentalReadings
*/
@Mapper
public interface EnvironmentalReadingsMapper {

    int deleteByPrimaryKey(String id);

    int insert(EnvironmentalReadings record);

    int insertSelective(EnvironmentalReadings record);

    EnvironmentalReadings selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EnvironmentalReadings record);

    int updateByPrimaryKey(EnvironmentalReadings record);

}
