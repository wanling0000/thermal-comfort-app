package com.wanling.trigger.api.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComfortLevelEnvironmentRangeDTO {
    private int comfortLevel;        // -2 ~ 2
    private double minTemp;
    private double maxTemp;
    private double minHumidity;
    private double maxHumidity;
    private int sampleCount;
}
