package com.wanling.domain.environmental.repository;

import java.util.List;

import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 06/05/2025
 * 23:07
 */
public interface IEnvironmentalReadingRepository {
    void saveEnvironmentalReadings(List<EnvironmentalReadingEntity> readings);
}
