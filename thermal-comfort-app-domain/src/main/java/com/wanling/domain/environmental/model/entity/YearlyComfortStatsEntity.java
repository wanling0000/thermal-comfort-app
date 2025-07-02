package com.wanling.domain.environmental.model.entity;

import java.util.List;

public record YearlyComfortStatsEntity(
        int year,
        List<DailyComfortStatEntity> data
){
}