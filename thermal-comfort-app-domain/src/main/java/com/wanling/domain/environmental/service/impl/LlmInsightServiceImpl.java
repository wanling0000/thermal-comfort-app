package com.wanling.domain.environmental.service.impl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.model.entity.llm.ActivityDistributionEntity;
import com.wanling.domain.environmental.model.entity.llm.ClothingDistributionEntity;
import com.wanling.domain.environmental.model.entity.llm.ComfortLevelEnvironmentRangeEntity;
import com.wanling.domain.environmental.model.entity.llm.MonthlyLLMInsightEntity;
import com.wanling.domain.environmental.model.entity.llm.TopLocationEntity;
import com.wanling.domain.environmental.model.entity.llm.TrendComparisonEntity;
import com.wanling.domain.environmental.repository.IComfortFeedbackRepository;
import com.wanling.domain.environmental.repository.IEnvironmentalReadingRepository;
import com.wanling.domain.environmental.service.ILlmInsightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlmInsightServiceImpl implements ILlmInsightService {

    private final IComfortFeedbackRepository comfortFeedbackRepository;
    private final IEnvironmentalReadingRepository environmentalReadingRepository;

    @Override
    public MonthlyLLMInsightEntity generateMonthlyInsight(String userId, LocalDate date) {
        // 1. 获取本月时间范围
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1).minusDays(1);

        List<ComfortFeedbackEntity> feedbacks = comfortFeedbackRepository.findByUserAndDateRange(userId, start, end);

        // 2. 统计各项
        int total = feedbacks.size();

        Map<Integer, Integer> comfortCounts = computeComfortCounts(feedbacks);
        Map<Integer, Double> comfortPercent = computeComfortPercentages(comfortCounts, total);

        List<ActivityDistributionEntity> activityDist = computeActivityDistribution(feedbacks);
        List<ClothingDistributionEntity> clothingDist = computeClothingDistribution(feedbacks);

        List<ComfortLevelEnvironmentRangeEntity> comfortLevelEnvRanges = computeComfortLevelEnvRanges(feedbacks);

        TrendComparisonEntity trend = computeTrendComparison(userId, start, end, feedbacks);
        TopLocationEntity topLocation = computeTopLocation(feedbacks);

        return new MonthlyLLMInsightEntity(
                start,
                end,
                total,
                comfortCounts,
                comfortPercent,
                activityDist,
                clothingDist,
                comfortLevelEnvRanges,
                trend,
                topLocation
        );
    }

    private Map<Integer, Integer> computeComfortCounts(List<ComfortFeedbackEntity> feedbacks) {
        return feedbacks.stream()
                        .collect(Collectors.groupingBy(
                                ComfortFeedbackEntity::getComfortLevel,
                                Collectors.reducing(0, e -> 1, Integer::sum)
                        ));
    }

    private Map<Integer, Double> computeComfortPercentages(Map<Integer, Integer> counts, int total) {
        if (total == 0) return Map.of();
        return counts.entrySet().stream()
                     .collect(Collectors.toMap(
                             Map.Entry::getKey,
                             e -> e.getValue() * 100.0 / total
                     ));
    }

    private List<ActivityDistributionEntity> computeActivityDistribution(List<ComfortFeedbackEntity> feedbacks) {
        Map<String, Long> grouped = feedbacks.stream()
                                             .map(ComfortFeedbackEntity::getActivityTypeId)
                                             .filter(Optional::isPresent)
                                             .map(Optional::get)
                                             .collect(Collectors.groupingBy(a -> a, Collectors.counting()));

        long total = grouped.values().stream().mapToLong(Long::longValue).sum();
        return grouped.entrySet().stream()
                      .map(e -> new ActivityDistributionEntity(
                              e.getKey(),
                              e.getValue().intValue(),
                              e.getValue() * 100.0 / total
                      ))
                      .toList();
    }


    private List<ClothingDistributionEntity> computeClothingDistribution(List<ComfortFeedbackEntity> feedbacks) {
        Map<String, Long> grouped = feedbacks.stream()
                                             .map(ComfortFeedbackEntity::getClothingLevel)
                                             .filter(Optional::isPresent)
                                             .map(Optional::get)
                                             .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        long total = grouped.values().stream().mapToLong(Long::longValue).sum();
        return grouped.entrySet().stream()
                      .map(e -> new ClothingDistributionEntity(
                              e.getKey(),
                              e.getValue().intValue(),
                              e.getValue() * 100.0 / total
                      ))
                      .toList();
    }

    private List<ComfortLevelEnvironmentRangeEntity> computeComfortLevelEnvRanges(List<ComfortFeedbackEntity> feedbacks) {
        // 按 comfortLevel 分组 -> List<readingId>
        Map<Integer, List<String>> grouped = feedbacks.stream()
                                                      .collect(Collectors.groupingBy(
                                                              ComfortFeedbackEntity::getComfortLevel,
                                                              Collectors.mapping(ComfortFeedbackEntity::getReadingId, Collectors.toList())
                                                      ));

        List<ComfortLevelEnvironmentRangeEntity> result = new java.util.ArrayList<>();

        for (int level = -2; level <= 2; level++) {
            List<String> readingIds = grouped.getOrDefault(level, List.of());
            if (readingIds.isEmpty()) {
                result.add(new ComfortLevelEnvironmentRangeEntity(level, 0, 0, 0, 0, 0));
                continue;
            }

            List<EnvironmentalReadingEntity> readings = environmentalReadingRepository.findByReadingIds(readingIds);
            List<Double> temps = readings.stream().map(EnvironmentalReadingEntity::getTemperature).toList();
            List<Double> humids = readings.stream().map(EnvironmentalReadingEntity::getHumidity).toList();

            double minTemp = temps.stream().mapToDouble(Double::doubleValue).min().orElse(0);
            double maxTemp = temps.stream().mapToDouble(Double::doubleValue).max().orElse(0);
            double minHumid = humids.stream().mapToDouble(Double::doubleValue).min().orElse(0);
            double maxHumid = humids.stream().mapToDouble(Double::doubleValue).max().orElse(0);

            result.add(new ComfortLevelEnvironmentRangeEntity(level, minTemp, maxTemp, minHumid, maxHumid, readingIds.size()));
        }

        return result;
    }

    private TrendComparisonEntity computeTrendComparison(String userId, LocalDate start, LocalDate end, List<ComfortFeedbackEntity> current) {
        LocalDate prevStart = start.minusMonths(1);
        LocalDate prevEnd = start.minusDays(1);

        var previous = comfortFeedbackRepository.findByUserAndDateRange(userId, prevStart, prevEnd);

        int currTotal = current.size();
        int prevTotal = previous.size();

        long currComfort = current.stream().filter(f -> f.getComfortLevel() == 0).count();
        long prevComfort = previous.stream().filter(f -> f.getComfortLevel() == 0).count();

        double currRatio = currTotal == 0 ? 0.0 : currComfort * 100.0 / currTotal;
        double prevRatio = prevTotal == 0 ? 0.0 : prevComfort * 100.0 / prevTotal;
        double delta = currRatio - prevRatio;

        return new TrendComparisonEntity(currTotal, currRatio, prevTotal, prevRatio, delta);
    }

    private TopLocationEntity computeTopLocation(List<ComfortFeedbackEntity> feedbacks) {
        if (feedbacks.isEmpty()) return null;

        var grouped = feedbacks.stream()
                               .collect(Collectors.groupingBy(
                                       f -> f.getCustomTagName().orElse(f.getLocationDisplayName())
                               ));

        var topEntry = grouped.entrySet().stream()
                              .max(Map.Entry.comparingByValue(Comparator.comparingInt(List::size)))
                              .orElseThrow();

        String name = topEntry.getKey();
        List<ComfortFeedbackEntity> group = topEntry.getValue();

        Optional<Double> lat = group.get(0).getRawLatitude();
        Optional<Double> lon = group.get(0).getRawLongitude();

        Map<Integer, Integer> comfortStats = group.stream()
                                                  .collect(Collectors.groupingBy(
                                                          ComfortFeedbackEntity::getComfortLevel,
                                                          Collectors.reducing(0, e -> 1, Integer::sum)
                                                  ));

        return new TopLocationEntity(name, lat.orElse(0.0), lon.orElse(0.0), comfortStats, group.size());
    }
}
