package com.wanling.trigger.api.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrendComparisonDTO {
    private int currentCount;
    private double currentComfortRatio;

    private int previousCount;
    private double previousComfortRatio;

    private double delta;
}