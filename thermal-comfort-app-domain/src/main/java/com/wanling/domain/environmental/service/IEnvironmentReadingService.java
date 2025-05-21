package com.wanling.domain.environmental.service;

import java.util.List;

import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 08/05/2025
 * 23:00
 */
public interface IEnvironmentReadingService {
    void uploadReadings(List<EnvironmentalReadingEntity> readings);
}
