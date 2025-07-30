package com.wanling.domain.environmental.model.entity.llm;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyLLMInsightEntity {

    private LocalDate startDate;
    private LocalDate endDate;

    private int totalFeedbackCount;

    private Map<Integer, Integer> comfortLevelCounts;
    private Map<Integer, Double> comfortLevelPercentages;

    private List<ActivityDistributionEntity> activityDistribution;
    private List<ClothingDistributionEntity> clothingDistribution;

    private List<ComfortLevelEnvironmentRangeEntity> comfortLevelEnvRanges;

    private TrendComparisonEntity trendComparison;

    private TopLocationEntity topLocation;
}