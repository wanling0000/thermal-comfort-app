package com.wanling.domain.environmental.service;

import java.time.LocalDate;
import java.util.List;

import com.wanling.domain.environmental.model.entity.ComfortStatisticsEntity;
import com.wanling.domain.environmental.model.entity.SummaryInsightEntity;
import com.wanling.domain.environmental.model.entity.YearlyComfortStatsEntity;
import com.wanling.domain.environmental.model.valobj.DailyChartPoint;
import com.wanling.types.enums.Resolution;

public interface IAnalyticsService {
    List<DailyChartPoint> queryDailyView(String userId, LocalDate date);

    ComfortStatisticsEntity getComfortStatistics(String userId, LocalDate date, Resolution resolution);

    YearlyComfortStatsEntity getYearlyStats(String userId, int year);

    SummaryInsightEntity getSummaryInsights(String userId, LocalDate date, Resolution resolution);
}

