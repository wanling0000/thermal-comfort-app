package com.wanling.trigger.api.dto;

public record EnvironmentalReadingDTO(
        String readingId,
        Long timestamp,
        Double temperature,
        Double humidity,
        Double battery,
        String sensorId,
        LocationDTO location
) {}

