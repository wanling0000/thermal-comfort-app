package com.wanling.trigger.assembler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.trigger.api.dto.EnvironmentalReadingDTO;
import com.wanling.trigger.api.dto.LocationDTO;

public class EnvironmentalReadingAssembler {
    public static EnvironmentalReadingEntity toEntity(EnvironmentalReadingDTO dto) {
        LocationCandidateVO candidate = null;

        if (dto.getLocation() != null) {
            LocationDTO loc = dto.getLocation();
            candidate = LocationCandidateVO.builder()
                                         .displayName(loc.getDisplayName())
                                         .latitude(loc.getLatitude())
                                         .longitude(loc.getLongitude())
                                         .isCustom(Boolean.TRUE.equals(loc.getIsCustom()))
                                         .customTag(loc.getCustomTag())
                                         .build();
        }

        String coordinates = (candidate != null)
                ? String.format("POINT(%f %f)", candidate.getLongitude(), candidate.getLatitude())
                : null;

        return EnvironmentalReadingEntity.builder()
                                         .readingId(dto.getReadingId())
                                         .timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getTimestamp()), ZoneId.systemDefault()))
                                         .temperature(dto.getTemperature())
                                         .humidity(dto.getHumidity())
                                         .battery(dto.getBattery())
                                         .sensorId(dto.getSensorId())
                                         .location(candidate) // 关键字段
                                         .rawCoordinates(coordinates)
                                         .build();
    }

}