package com.wanling.infrastructure.persistent.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.repository.IEnvironmentalReadingRepository;
import com.wanling.infrastructure.persistent.mapper.EnvironmentalReadingsMapper;
import com.wanling.infrastructure.persistent.po.EnvironmentalReadings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 06/05/2025
 * 23:08
 */

@Slf4j
@Repository
public class EnvironmentalReadingRepository implements IEnvironmentalReadingRepository {
    @Resource
    private EnvironmentalReadingsMapper environmentalReadingsMapper;

    @Override
    public void saveEnvironmentalReadings(List<EnvironmentalReadingEntity> readings) {
        List<EnvironmentalReadings> poList = readings.stream().map(entity -> {
            EnvironmentalReadings reading = new EnvironmentalReadings();
            reading.setReadingId(entity.getReadingId() != null ? entity.getReadingId() :UUID.randomUUID().toString());
            reading.setTimestamp(entity.getTimestamp());
            reading.setTemperature(entity.getTemperature());
            reading.setHumidity(entity.getHumidity());
            reading.setSensorId(entity.getSensorId());
            reading.setLocationTagId(entity.getLocationTagId());
            reading.setRawCoordinates(entity.getRawCoordinates());
            reading.setUserId(entity.getUserId());
            return reading;
        }).collect(Collectors.toList());
        for (EnvironmentalReadings po : poList) {
            environmentalReadingsMapper.insert(po);
        }
    }

    @Override
    public List<EnvironmentalReadingEntity> findByUserIdAndTimeRange(String userId, LocalDateTime start, LocalDateTime end) {
        List<EnvironmentalReadings> poList = environmentalReadingsMapper.findByUserIdAndTimeRange(userId, start, end);

        return poList.stream()
                     .map(po -> EnvironmentalReadingEntity.builder()
                                                          .readingId(po.getReadingId())
                                                          .timestamp(po.getTimestamp())
                                                          .temperature(po.getTemperature())
                                                          .humidity(po.getHumidity())
                                                          .build()
                     )
                     .toList();
    }

    @Override
    public List<EnvironmentalReadingEntity> findByReadingIds(List<String> readingIds) {
        // 1. 调用 Mapper 层方法，根据 readingId 批量查询 PO 列表
        List<EnvironmentalReadings> poList = environmentalReadingsMapper.findByReadingIds(readingIds);

        // 2. 将 PO 转换为领域实体 EnvironmentalReadingEntity
        return poList.stream()
                     .map(po -> EnvironmentalReadingEntity.builder()
                                                          .readingId(po.getReadingId())           // 设置读取 ID
                                                          .timestamp(po.getTimestamp())           // 设置时间戳
                                                          .temperature(po.getTemperature())       // 设置温度
                                                          .humidity(po.getHumidity())             // 设置湿度
                                                          .sensorId(po.getSensorId())             // 设置传感器 ID
                                                          .locationTagId(po.getLocationTagId())   // 设置地点标签 ID
                                                          .rawCoordinates(po.getRawCoordinates()) // 原始坐标（WKT）
                                                          .build()
                     )
                     .collect(Collectors.toList()); // 3. 转为列表返回
    }

    @Override
    public EnvironmentalReadingEntity findById(String readingId) {
        EnvironmentalReadings po = environmentalReadingsMapper.selectByPrimaryKey(readingId);

        if (po == null) {
            return null;
        }

        return EnvironmentalReadingEntity.builder()
                                         .readingId(po.getReadingId())
                                         .timestamp(po.getTimestamp())
                                         .temperature(po.getTemperature())
                                         .humidity(po.getHumidity())
                                         .sensorId(po.getSensorId())
                                         .locationTagId(po.getLocationTagId())
                                         .rawCoordinates(po.getRawCoordinates())
                                         .userId(po.getUserId())
                                         .build();
    }
}
