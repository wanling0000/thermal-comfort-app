package com.wanling.domain.environmental.model.entity.llm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityDistributionEntity {
    private String activityTypeId; // 如 sitting、walking
    private int count;
    private double percentage;
}