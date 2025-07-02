package com.wanling.trigger.assembler;
import java.util.List;
import java.util.Optional;

import com.wanling.domain.environmental.model.entity.ComfortStatisticsEntity;
import com.wanling.domain.environmental.model.entity.InsightCardEntity;
import com.wanling.domain.environmental.model.entity.LocationInsightEntity;
import com.wanling.domain.environmental.model.entity.SummaryInsightEntity;
import com.wanling.domain.environmental.model.entity.YearlyComfortStatsEntity;
import com.wanling.domain.environmental.model.valobj.DailyChartPoint;
import com.wanling.trigger.api.dto.analysis.ComfortStatisticsDTO;
import com.wanling.trigger.api.dto.analysis.DailyChartPointDTO;
import com.wanling.trigger.api.dto.analysis.DailyComfortStatDTO;
import com.wanling.trigger.api.dto.FeedbackInfoDTO;
import com.wanling.trigger.api.dto.analysis.InsightCardDTO;
import com.wanling.trigger.api.dto.analysis.LocationInsightDTO;
import com.wanling.trigger.api.dto.analysis.SummaryInsightResponseDTO;
import com.wanling.trigger.api.dto.analysis.YearlyComfortStatsDTO;

public class AnalyticsAssembler {

    public static DailyChartPointDTO toDailyChartPointDTO(DailyChartPoint point) {
        FeedbackInfoDTO feedbackDTO = point.feedback()
                                           .map(fb -> new FeedbackInfoDTO(
                                                   fb.feedbackId(),
                                                   fb.comfortLevel(),
                                                   fb.notes(),
                                                   fb.activityTypeId(),
                                                   fb.clothingLevel()))
                                           .orElse(null);

        return new DailyChartPointDTO(
                point.timestamp(),
                point.temperature(),
                point.humidity(),
                feedbackDTO
        );
    }

    public static ComfortStatisticsDTO toComfortStatisticsDTO(ComfortStatisticsEntity entity) {
        return new ComfortStatisticsDTO(entity.total(), entity.toLevelCountMap());
    }

    public static YearlyComfortStatsDTO toYearlyComfortStatsDTO(YearlyComfortStatsEntity entity) {
        List<DailyComfortStatDTO> dailyStats = entity.data().stream()
                                                     .map(stat -> new DailyComfortStatDTO(
                                                             stat.date().toString(),
                                                             stat.averageComfort().orElse(null),
                                                             stat.feedbackCount()
                                                     ))
                                                     .toList();

        return new YearlyComfortStatsDTO(entity.year(), dailyStats);
    }

    public static SummaryInsightResponseDTO toSummaryInsightDTO(SummaryInsightEntity entity) {
        return new SummaryInsightResponseDTO(
                entity.resolution(),
                entity.startDate(),
                entity.endDate(),
                entity.insights().stream().map(AnalyticsAssembler::toInsightCardDTO).toList(),
                entity.locationInsights().isEmpty() ? Optional.empty() :
                        Optional.of(entity.locationInsights().stream()
                                          .map(AnalyticsAssembler::toLocationInsightDTO).toList())
        );
    }

    public static InsightCardDTO toInsightCardDTO(InsightCardEntity entity) {
        return new InsightCardDTO(
                entity.title(),
                entity.content(),
                entity.type()
        );
    }

    public static LocationInsightDTO toLocationInsightDTO(LocationInsightEntity entity) {
        ComfortStatisticsEntity stats = entity.comfortStats();

        return new LocationInsightDTO(
                entity.locationName(),
                entity.latitude(),
                entity.longitude(),
                new ComfortStatisticsDTO(
                        stats.total(),
                        stats.toLevelCountMap()
                )
        );
    }

}
