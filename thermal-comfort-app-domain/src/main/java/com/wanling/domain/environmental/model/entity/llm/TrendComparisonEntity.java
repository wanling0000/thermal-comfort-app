package com.wanling.domain.environmental.model.entity.llm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrendComparisonEntity {
    private int currentCount;
    private double currentComfortRatio;

    private int previousCount;
    private double previousComfortRatio;

    private double delta;
}