package com.wanling.domain.environmental.model.entity.llm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClothingDistributionEntity {
    private String clothingLevel; // 如 heavy、light
    private int count;
    private double percentage;
}