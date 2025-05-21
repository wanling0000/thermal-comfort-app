package com.wanling.trigger.api.dto;

import lombok.Data;

@Data
public class EnvironmentalReadingDTO {
    private String readingId;
    private Long timestamp; // Unix milliseconds
    private Double temperature;
    private Double humidity;
    private Double battery;
    private String sensorId;
    private LocationDTO location;
}
