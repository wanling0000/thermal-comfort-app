package com.wanling.backend.mapper;

import com.wanling.backend.entity.BleData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BleDataMapper {
    void insert(BleData bleData);
}
