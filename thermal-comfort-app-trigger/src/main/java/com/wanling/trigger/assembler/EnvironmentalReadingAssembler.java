package com.wanling.trigger.assembler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.trigger.api.dto.EnvironmentalReadingDTO;
import com.wanling.trigger.api.dto.LocationDTO;

public class EnvironmentalReadingAssembler {
    public static EnvironmentalReadingEntity toEntity(EnvironmentalReadingDTO dto, String userId) {
        LocationCandidateVO candidate = null;

        if (dto.location() != null) {
            LocationDTO loc = dto.location();
            candidate = LocationCandidateVO.builder()
                                         .displayName(loc.displayName())
                                         .latitude(loc.latitude())
                                         .longitude(loc.longitude())
                                         .isCustom(Boolean.TRUE.equals(loc.isCustom()))
                                         .customTag(loc.customTag())
                                         .build();
        }

        String coordinates = (candidate != null)
                ? String.format("POINT(%f %f)", candidate.getLongitude(), candidate.getLatitude())
                : null;

        return EnvironmentalReadingEntity.builder()
                                         .readingId(dto.readingId())
                                         .timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.timestamp()), ZoneId.systemDefault()))
                                         .temperature(dto.temperature())
                                         .humidity(dto.humidity())
                                         .battery(dto.battery())
                                         .sensorId(dto.sensorId())
                                         .location(candidate)
                                         .rawCoordinates(coordinates)
                                         .userId(userId)
                                         .build();
    }

}