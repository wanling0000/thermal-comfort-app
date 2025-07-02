package com.wanling.domain.environmental.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.types.jackson.UnixMillisToLocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentalReadingEntity {
    private String readingId;
    @JsonDeserialize(using = UnixMillisToLocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;
    private Double temperature;
    private Double humidity;
    private Double battery;
    private String sensorId;
    private String locationTagId;
    private String rawCoordinates;
    private String userId;

    private LocationCandidateVO location; // value object, contains displayName + lat + lng + isCustom + customTag
}