package com.wanling.domain.environmental.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorEntity {
    private String sensorId;
    private String name;
    private String type;
    private LocalDateTime lastReadingTime;
    private String userId;
    private String macAddress;
}