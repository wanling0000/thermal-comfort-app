package com.wanling.domain.environmental.repository;

import java.time.LocalDateTime;
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

    List<EnvironmentalReadingEntity> findByUserIdAndTimeRange(String userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<EnvironmentalReadingEntity> findByReadingIds(List<String> readingIds);
}
