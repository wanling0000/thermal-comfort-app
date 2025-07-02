package com.wanling.domain.environmental.model.entity;

import java.time.LocalDate;
import java.util.List;

import com.wanling.types.enums.Resolution;

public record SummaryInsightEntity(
    Resolution resolution,
    LocalDate startDate,
    LocalDate endDate,
    List<InsightCardEntity> insights,
    List<LocationInsightEntity> locationInsights
) {}