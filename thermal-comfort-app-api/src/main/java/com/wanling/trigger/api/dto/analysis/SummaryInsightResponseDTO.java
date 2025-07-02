package com.wanling.trigger.api.dto.analysis;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.wanling.types.enums.Resolution;

public record SummaryInsightResponseDTO(
        Resolution resolution,
        LocalDate startDate,
        LocalDate endDate,
        List<InsightCardDTO> insights,
        Optional<List<LocationInsightDTO>> locationInsights// 地图专用
) {}
