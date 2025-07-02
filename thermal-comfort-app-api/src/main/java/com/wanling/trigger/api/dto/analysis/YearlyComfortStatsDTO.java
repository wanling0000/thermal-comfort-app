package com.wanling.trigger.api.dto.analysis;

import java.util.List;

public record YearlyComfortStatsDTO(
    int year,
    List<DailyComfortStatDTO> data
) {}