package com.wanling.trigger.api.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClothingDistributionDTO {
    private String clothingLevel; // 如 heavy、light
    private int count;
    private double percentage;
}