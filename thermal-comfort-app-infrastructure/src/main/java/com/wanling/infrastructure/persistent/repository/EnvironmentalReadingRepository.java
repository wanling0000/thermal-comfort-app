package com.wanling.infrastructure.persistent.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.repository.IEnvironmentalReadingRepository;
import com.wanling.infrastructure.persistent.mapper.EnvironmentalReadingsMapper;
import com.wanling.infrastructure.persistent.po.EnvironmentalReadings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
            return reading;
        }).collect(Collectors.toList());
        for (EnvironmentalReadings po : poList) {
            environmentalReadingsMapper.insert(po);
        }
    }
}
