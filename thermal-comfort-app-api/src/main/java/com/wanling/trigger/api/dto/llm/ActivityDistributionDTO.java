package com.wanling.trigger.api.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityDistributionDTO {
    private String activityTypeId; // 如 sitting、walking
    private int count;
    private double percentage;
}