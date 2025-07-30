package com.wanling.trigger.assembler;

import java.util.List;
import java.util.stream.Collectors;

import com.wanling.domain.environmental.model.entity.llm.ComfortLevelEnvironmentRangeEntity;
import com.wanling.domain.environmental.model.entity.llm.MonthlyLLMInsightEntity;
import com.wanling.domain.environmental.model.entity.llm.TopLocationEntity;
import com.wanling.domain.environmental.model.entity.llm.TrendComparisonEntity;
import com.wanling.trigger.api.dto.llm.ActivityDistributionDTO;
import com.wanling.trigger.api.dto.llm.ClothingDistributionDTO;
import com.wanling.trigger.api.dto.llm.ComfortLevelEnvironmentRangeDTO;
import com.wanling.trigger.api.dto.llm.MonthlyLLMInsightDTO;
import com.wanling.trigger.api.dto.llm.TopLocationDTO;
import com.wanling.trigger.api.dto.llm.TrendComparisonDTO;

public class LlmAssembler {

    public static MonthlyLLMInsightDTO toMonthlyLLMInsightDTO(MonthlyLLMInsightEntity entity) {
        if (entity == null) return null;

        MonthlyLLMInsightDTO dto = new MonthlyLLMInsightDTO();

        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setTotalFeedbackCount(entity.getTotalFeedbackCount());
        dto.setComfortLevelCounts(entity.getComfortLevelCounts());
        dto.setComfortLevelPercentages(entity.getComfortLevelPercentages());

        dto.setActivityDistribution(entity.getActivityDistribution().stream()
                .map(e -> new ActivityDistributionDTO(e.getActivityTypeId(), e.getCount(), e.getPercentage()))
                .collect(Collectors.toList()));

        dto.setClothingDistribution(entity.getClothingDistribution().stream()
                .map(e -> new ClothingDistributionDTO(e.getClothingLevel(), e.getCount(), e.getPercentage()))
                .collect(Collectors.toList()));

        List<ComfortLevelEnvironmentRangeEntity> comfortLevelEnvRanges = entity.getComfortLevelEnvRanges();
        dto.setComfortLevelEnvRanges(
                comfortLevelEnvRanges.stream()
                                     .map(e -> new ComfortLevelEnvironmentRangeDTO(
                                             e.getComfortLevel(),
                                             e.getMinTemp(),
                                             e.getMaxTemp(),
                                             e.getMinHumidity(),
                                             e.getMaxHumidity(),
                                             e.getSampleCount()
                                     ))
                                     .collect(Collectors.toList())
        );

        TrendComparisonEntity trend = entity.getTrendComparison();
        dto.setTrendComparison(new TrendComparisonDTO(
                trend.getCurrentCount(),
                trend.getCurrentComfortRatio(),
                trend.getPreviousCount(),
                trend.getPreviousComfortRatio(),
                trend.getDelta()
        ));

        TopLocationEntity top = entity.getTopLocation();
        if (top != null) {
            dto.setTopLocation(new TopLocationDTO(
                    top.getName(),
                    top.getLatitude(),
                    top.getLongitude(),
                    top.getComfortStats(),
                    top.getTotalCount()
            ));
        }

        return dto;
    }
}