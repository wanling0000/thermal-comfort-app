package com.wanling.trigger.api.dto.llm;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class MonthlyLLMInsightDTO {

    private LocalDate startDate;
    private LocalDate endDate;

    // 总反馈数量
    private int totalFeedbackCount;

    // 每个舒适度等级的数量和占比（-2 ~ 2）
    private Map<Integer, Integer> comfortLevelCounts;
    private Map<Integer, Double> comfortLevelPercentages;

    // 活动类型分布
    private List<ActivityDistributionDTO> activityDistribution;

    // 衣着等级分布
    private List<ClothingDistributionDTO> clothingDistribution;

    private List<ComfortLevelEnvironmentRangeDTO> comfortLevelEnvRanges;

    // 与上月对比的舒适度趋势
    private TrendComparisonDTO trendComparison;

    // 最常出现的地点统计
    private TopLocationDTO topLocation;
}