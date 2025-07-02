package com.wanling.domain.environmental.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.entity.ComfortStatisticsEntity;
import com.wanling.domain.environmental.model.entity.DailyComfortStatEntity;
import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.model.entity.InsightCardEntity;
import com.wanling.domain.environmental.model.entity.LocationInsightEntity;
import com.wanling.domain.environmental.model.entity.SummaryInsightEntity;
import com.wanling.domain.environmental.model.entity.YearlyComfortStatsEntity;
import com.wanling.domain.environmental.model.valobj.DailyChartPoint;
import com.wanling.domain.environmental.model.valobj.FeedbackSnapshot;
import com.wanling.domain.environmental.repository.IComfortFeedbackRepository;
import com.wanling.domain.environmental.repository.IEnvironmentalReadingRepository;
import com.wanling.domain.environmental.repository.IUserLocationRepository;
import com.wanling.domain.environmental.service.IAnalyticsService;
import com.wanling.types.enums.InsightType;
import com.wanling.types.enums.Resolution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements IAnalyticsService {
    private final IEnvironmentalReadingRepository environmentalReadingRepository;
    private final IComfortFeedbackRepository comfortFeedbackRepository;
    private final IUserLocationRepository userLocationRepository;

    @Override
    public List<DailyChartPoint> queryDailyView(String userId, LocalDate date) {
        // 1. 定义时间范围
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        // 2. 查询readings和feedbacks
        List<EnvironmentalReadingEntity> readings =
                environmentalReadingRepository.findByUserIdAndTimeRange(userId, startOfDay, endOfDay);

        List<FeedbackSnapshot> feedbacks =
                comfortFeedbackRepository.findSnapshotsByUserIdAndTimeRange(userId, startOfDay, endOfDay);

        // 用 readingId 构造 feedbackMap
        Map<String, FeedbackSnapshot> feedbackMap = feedbacks.stream()
                                                             .collect(Collectors.toMap(FeedbackSnapshot::readingId, Function.identity()));

        // 然后组装 ChartPoint
        return readings.stream()
                       .map(reading -> {
                           Optional<FeedbackSnapshot> snapshot = Optional.ofNullable(
                                   feedbackMap.get(reading.getReadingId())
                           );

                           return new DailyChartPoint(
                                   reading.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli(),
                                   reading.getTemperature(),
                                   reading.getHumidity(),
                                   snapshot
                           );
                       })
                       .toList();

    }

    @Override
    public ComfortStatisticsEntity getComfortStatistics(String userId, LocalDate date, Resolution resolution) {
        LocalDate start;
        LocalDate end;

        switch (resolution) {
            case WEEK -> {
                start = date.with(java.time.DayOfWeek.MONDAY);
                end = start.plusDays(7);
            }
            case MONTH -> {
                start = date.withDayOfMonth(1);
                end = start.plusMonths(1);
            }
            default -> throw new IllegalArgumentException("Unsupported resolution: " + resolution);
        }

        LocalDateTime from = start.atStartOfDay();
        LocalDateTime to = end.atStartOfDay();

        List<FeedbackSnapshot> snapshots = comfortFeedbackRepository.findSnapshotsByUserIdAndTimeRange(userId, from, to);

        return summarize(snapshots);
    }

    @Override
    public YearlyComfortStatsEntity getYearlyStats(String userId, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = start.withDayOfYear(start.lengthOfYear());

        List<DailyComfortStatEntity> rawStats = comfortFeedbackRepository.findDailyStatsForUserInYear(userId, start, end);

        // 转为 map 便于补齐
        Map<LocalDate, DailyComfortStatEntity> statMap = rawStats.stream()
                                                                 .collect(Collectors.toMap(DailyComfortStatEntity::date, Function.identity()));

        List<DailyComfortStatEntity> filled = new ArrayList<>();

        LocalDate current = start;
        while (!current.isAfter(end)) {
            DailyComfortStatEntity stat = statMap.getOrDefault(
                    current,
                    new DailyComfortStatEntity(current, Optional.empty(), 0)
            );
            filled.add(stat);
            current = current.plusDays(1);
        }

        return new YearlyComfortStatsEntity(year, filled);
    }

    @Override
    public SummaryInsightEntity getSummaryInsights(String userId, LocalDate date, Resolution resolution) {
        return switch (resolution) {
            case DAILY -> buildDailySummary(userId, date);
            case WEEK, MONTH -> buildPeriodSummary(userId, date, resolution);
            case YEAR -> buildYearlySummary(userId, date);
        };
    }

    private SummaryInsightEntity buildYearlySummary(String userId, LocalDate date) {
        // 1. 获取当前年份
        int year = date.getYear();

        // 2. 构造当年起止日期
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        // 3. 查询该用户该年所有原始 ComfortFeedback 数据
        List<ComfortFeedbackEntity> feedbacks = comfortFeedbackRepository.findByUserAndDateRange(userId, start, end);

        // 4. 统计舒适度分布（用于主卡片）
        ComfortStatisticsEntity stats = summarizeFeedback(feedbacks); // 自定义方法（见下）

        // 5. 构造 InsightCard 内容
        String title = "Your comfort overview this year";
        String content = String.format(
                "You've submitted feedback on %d days. Comfortable: %d%%, Too Hot: %d%%, Too Cold: %d%%.",
                feedbacks.stream()
                         .map(f -> f.getTimestamp().toLocalDate())
                         .distinct()
                         .count(), // 不重复的天数
                stats.countComfort() * 100 / Math.max(1, stats.total()),
                stats.countTooHot() * 100 / Math.max(1, stats.total()),
                stats.countTooCold() * 100 / Math.max(1, stats.total())
        );

        InsightCardEntity overviewCard = new InsightCardEntity(title, content, InsightType.COMFORT_LEVEL);

        // 6. 构造 top location 卡片
        InsightCardEntity topLocationCard = buildTopLocationInsight(feedbacks, Resolution.YEAR);

        // 7. 构造地点级统计信息（与月/周结构一致）
        List<LocationInsightEntity> locationInsights = buildLocationInsights(feedbacks);

        // 8. 返回封装对象
        return new SummaryInsightEntity(
                Resolution.YEAR,
                start,
                end,
                List.of(overviewCard, topLocationCard),
                locationInsights
        );
    }

    private SummaryInsightEntity buildPeriodSummary(String userId, LocalDate date, Resolution resolution) {
        // 1. 计算起止日期
        LocalDate start = switch (resolution) {
            case WEEK -> date.with(DayOfWeek.MONDAY);
            case MONTH -> date.withDayOfMonth(1);
            default -> throw new IllegalArgumentException("Unsupported resolution: " + resolution);
        };
        LocalDate end = switch (resolution) {
            case WEEK -> start.plusDays(6);
            case MONTH -> start.plusMonths(1).minusDays(1);
            default -> throw new IllegalArgumentException("Unsupported resolution: " + resolution);
        };

        List<ComfortFeedbackEntity> feedbacks = comfortFeedbackRepository.findByUserAndDateRange(userId, start, end);

        List<InsightCardEntity> insights = List.of(
                buildComfortRangeInsight(feedbacks, resolution),
                buildActivityDistributionInsight(feedbacks, resolution),
                buildTrendInsight(userId, start, end, feedbacks, resolution),
                buildTopLocationInsight(feedbacks, resolution)
        );

        List<LocationInsightEntity> locationInsights = buildLocationInsights(feedbacks);

        return new SummaryInsightEntity(Resolution.WEEK, start, end, insights, locationInsights);
    }

    private List<LocationInsightEntity> buildLocationInsights(List<ComfortFeedbackEntity> feedbacks) {
        // 1. 按地点名称分组（优先用自定义标签名，否则用系统 displayName）
        Map<String, List<ComfortFeedbackEntity>> grouped = feedbacks.stream()
                                                                    .collect(Collectors.groupingBy(e -> e.getCustomTagName().orElse(e.getLocationDisplayName())));

        // 2. 遍历每组地点反馈，构造统计对象
        return grouped.entrySet().stream()
                      .map(entry -> {
                          String name = entry.getKey();                // 地点名
                          List<ComfortFeedbackEntity> group = entry.getValue();

                          // 3. 统计每种舒适度等级的数量（-2 ~ 2）
                          int tooCold = 0, cold = 0, comfort = 0, warm = 0, tooHot = 0;
                          for (ComfortFeedbackEntity e : group) {
                              switch (e.getComfortLevel()) {
                                  case -2 -> tooCold++;
                                  case -1 -> cold++;
                                  case 0 -> comfort++;
                                  case 1 -> warm++;
                                  case 2 -> tooHot++;
                              }
                          }

                          // 4. 取第一条的经纬度作为代表位置
                          Optional<Double> lat = group.get(0).getRawLatitude();
                          Optional<Double> lon = group.get(0).getRawLongitude();

                          // 5. 构建 ComfortStatisticsEntity
                          ComfortStatisticsEntity stats = new ComfortStatisticsEntity(
                                  group.size(), tooCold, cold, comfort, warm, tooHot
                          );

                          // 6. 构建最终地点统计实体
                          return new LocationInsightEntity(
                                  name,
                                  lat.orElse(0.0),
                                  lon.orElse(0.0),
                                  stats
                          );
                      })
                      .toList();
    }

    private InsightCardEntity buildTrendInsight(String userId, LocalDate start, LocalDate end, List<ComfortFeedbackEntity> current, Resolution resolution) {
        String timeLabel = getTimeLabel(resolution);
        String prevLabel = switch (resolution) {
            case WEEK -> "last week";
            case MONTH -> "last month";
            default -> "previous period";
        };

        LocalDate lastStart = switch (resolution) {
            case WEEK -> start.minusWeeks(1);
            case MONTH -> start.minusMonths(1);
            default -> start.minusDays(7);
        };
        LocalDate lastEnd = switch (resolution) {
            case WEEK -> end.minusWeeks(1);
            case MONTH -> end.minusMonths(1).plusDays(end.lengthOfMonth() - 1);
            default -> end.minusDays(1);
        };

        List<ComfortFeedbackEntity> previous = comfortFeedbackRepository.findByUserAndDateRange(userId, lastStart, lastEnd);

        if (previous.isEmpty() || current.isEmpty()) {
            return new InsightCardEntity(
                    "Trend Summary",
                    "Insufficient data for comparison",
                    InsightType.COMPARISON
            );
        }

        long thisComfort = current.stream().filter(f -> f.getComfortLevel() == 0).count();
        long prevComfort = previous.stream().filter(f -> f.getComfortLevel() == 0).count();

        double thisRatio = thisComfort * 100.0 / current.size();
        double prevRatio = prevComfort * 100.0 / previous.size();

        double delta = thisRatio - prevRatio;

        String content = delta >= 0
                ? String.format("You felt comfortable %.1f%% more often than %s", delta, prevLabel)
                : String.format("You felt comfortable %.1f%% less than %s", -delta, prevLabel);

        return new InsightCardEntity("Comfort Trend " + timeLabel, content, InsightType.COMPARISON);
    }

    private InsightCardEntity buildTopLocationInsight(List<ComfortFeedbackEntity> feedbacks, Resolution resolution) {
        String timeLabel = getTimeLabel(resolution);
        if (feedbacks.isEmpty()) {
            return new InsightCardEntity(
                    "No location data " + timeLabel,
                    "-",
                    InsightType.LOCATION
            );
        }

        // 自定义优先，否则用 displayName
        String topLocationName = feedbacks.stream()
                                          .map(e -> e.getCustomTagName().orElse(e.getLocationDisplayName()))
                                          .collect(Collectors.groupingBy(name -> name, Collectors.counting()))
                                          .entrySet().stream()
                                          .max(Map.Entry.comparingByValue())
                                          .map(Map.Entry::getKey)
                                          .orElse("Unknown");

        return new InsightCardEntity("Top Location " + timeLabel,  topLocationName, InsightType.LOCATION);
    }

    private InsightCardEntity buildActivityDistributionInsight(List<ComfortFeedbackEntity> feedbacks, Resolution resolution) {
        String timeLabel = getTimeLabel(resolution);
        if (feedbacks.isEmpty()) {
            return new InsightCardEntity("No activity data " + timeLabel, "-", InsightType.ACTIVITY);
        }

        // 提取非空 activityTypeId
        Map<String, Long> grouped = feedbacks.stream()
                                             .map(ComfortFeedbackEntity::getActivityTypeId)
                                             .filter(Optional::isPresent)
                                             .map(Optional::get)
                                             .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        // 找出频率最高的活动
        String topActivity = grouped.entrySet().stream()
                                    .max(Map.Entry.comparingByValue())
                                    .map(Map.Entry::getKey)
                                    .orElse("-");

        long total = grouped.values().stream().mapToLong(Long::longValue).sum();
        long count = grouped.getOrDefault(topActivity, 0L);

        // 拼接内容
        String content = String.format("%s (%.0f%%)", topActivity, count * 100.0 / total);

        return new InsightCardEntity("Most frequent activity " + timeLabel, content, InsightType.ACTIVITY);
    }


    private InsightCardEntity buildComfortRangeInsight(List<ComfortFeedbackEntity> feedbacks, Resolution resolution) {
        String timeLabel = getTimeLabel(resolution);
        if (feedbacks.isEmpty()) {
            return new InsightCardEntity(
                    "No comfort data " + timeLabel,
                    "You didn't submit any comfort reports " + timeLabel + ".",
                    InsightType.TEMPERATURE_RANGE
            );
        }

        // 筛选出 comfortLevel == 0 的反馈
        List<String> readingIds = feedbacks.stream()
                                           .filter(f -> f.getComfortLevel() == 0)
                                           .map(ComfortFeedbackEntity::getReadingId)
                                           .distinct()
                                           .toList();

        // 若没有符合条件的记录
        if (readingIds.isEmpty()) {
            return new InsightCardEntity(
                    "No comfortable reports " + timeLabel,
                    "You submitted " + feedbacks.size() + " reports, but none felt comfortable.",
                    InsightType.TEMPERATURE_RANGE
            );
        }

        // 从 reading 表中获取温湿度数据
        List<EnvironmentalReadingEntity> readings = environmentalReadingRepository.findByReadingIds(readingIds);

        // 取出温度和湿度
        List<Double> temps = readings.stream().map(EnvironmentalReadingEntity::getTemperature).toList();
        List<Double> humids = readings.stream().map(EnvironmentalReadingEntity::getHumidity).toList();

        // 计算范围
        double minTemp = temps.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double maxTemp = temps.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double minHumid = humids.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double maxHumid = humids.stream().mapToDouble(Double::doubleValue).max().orElse(0);

        // 占比 = comfort / total
        long total = feedbacks.size();
        long comfortCount = readingIds.size(); // 注意已筛选为 comfort == 0
        double ratio = comfortCount * 100.0 / total;

        // 拼接内容
        String content = String.format(
                "%.0f°C – %.0f°C / %.0f%% – %.0f%% from %d comfortable reports %s",
                minTemp, maxTemp, minHumid, maxHumid, readingIds.size(), timeLabel
        );

        return new InsightCardEntity(
                "Your comfortable temperature & humidity range",
                content,
                InsightType.TEMPERATURE_RANGE
        );
    }

    public SummaryInsightEntity buildDailySummary(String userId, LocalDate date) {
        List<ComfortFeedbackEntity> feedbacks = comfortFeedbackRepository.findByUserAndDate(userId, date);

        InsightCardEntity avgComfort = buildAvgComfortCard(feedbacks);
        InsightCardEntity topActivity = buildTopActivityCard(feedbacks);
        InsightCardEntity topLocation = buildTopLocationCard(feedbacks);

        return new SummaryInsightEntity(
                Resolution.DAILY,
                date,
                date,
                List.of(avgComfort, topActivity, topLocation),
                Collections.emptyList()
        );
    }

    private ComfortStatisticsEntity summarize(List<FeedbackSnapshot> snapshots) {
        int tooCold = 0, cold = 0, comfort = 0, warm = 0, tooHot = 0;

        for (FeedbackSnapshot s : snapshots) {
            switch (s.comfortLevel()) {
                case -2 -> tooCold++;
                case -1 -> cold++;
                case 0  -> comfort++;
                case 1  -> warm++;
                case 2  -> tooHot++;
            }
        }

        int total = tooCold + cold + comfort + warm + tooHot;
        return new ComfortStatisticsEntity(total, tooCold, cold, comfort, warm, tooHot);
    }

    private ComfortStatisticsEntity summarizeFeedback(List<ComfortFeedbackEntity> feedbacks) {
        int tooCold = 0, cold = 0, comfort = 0, warm = 0, tooHot = 0;
        for (ComfortFeedbackEntity e : feedbacks) {
            switch (e.getComfortLevel()) {
                case -2 -> tooCold++;
                case -1 -> cold++;
                case 0  -> comfort++;
                case 1  -> warm++;
                case 2  -> tooHot++;
            }
        }
        int total = tooCold + cold + comfort + warm + tooHot;
        return new ComfortStatisticsEntity(total, tooCold, cold, comfort, warm, tooHot);
    }


    private InsightCardEntity buildAvgComfortCard(List<ComfortFeedbackEntity> feedbacks) {
        if (feedbacks.isEmpty()) {
            return new InsightCardEntity(
                    "No comfort feedback today",
                    "-",
                    InsightType.COMFORT_LEVEL
            );
        }

        double average = feedbacks.stream()
                                  .mapToInt(ComfortFeedbackEntity::getComfortLevel)
                                  .average()
                                  .orElse(0.0);

        int rounded = (int) Math.round(average);
        String emoji = switch (rounded) {
            case -2 -> "🥶 Too Cold";
            case -1 -> "🧊 Cold";
            case 0  -> "😊 Comfortable";
            case 1  -> "🌤️ Warm";
            case 2  -> "🥵 Too Hot";
            default -> String.format("%.2f", average); // fallback
        };

        return new InsightCardEntity("Your average comfort level today", emoji, InsightType.COMFORT_LEVEL);
    }

    private InsightCardEntity buildTopActivityCard(List<ComfortFeedbackEntity> feedbacks) {
        if (feedbacks.isEmpty()) {
            return new InsightCardEntity(
                    "No activity data today",
                    "-",
                    InsightType.ACTIVITY
            );
        }

        String topActivity = feedbacks.stream()
                                      .map(ComfortFeedbackEntity::getActivityTypeId)  // 得到 Optional<String>
                                      .filter(Optional::isPresent)                    // 过滤空值
                                      .map(Optional::get)                             // 拿到实际值
                                      .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                                      .entrySet().stream()
                                      .max(Map.Entry.comparingByValue())
                                      .map(Map.Entry::getKey)
                                      .orElse("-");

        return new InsightCardEntity(
                "Most frequent activity",
                topActivity,
                InsightType.ACTIVITY
        );
    }

    private InsightCardEntity buildTopLocationCard(List<ComfortFeedbackEntity> feedbacks) {
        if (feedbacks.isEmpty()) {
            return new InsightCardEntity(
                    "No location data today",
                    "-",
                    InsightType.LOCATION
            );
        }

        // 直接根据 displayName 或 customTagName 做分组统计
        String topLocationName = feedbacks.stream()
                                          .map(e -> e.getCustomTagName().orElse(e.getLocationDisplayName()))
                                          .collect(Collectors.groupingBy(name -> name, Collectors.counting()))
                                          .entrySet().stream()
                                          .max(Map.Entry.comparingByValue())
                                          .map(Map.Entry::getKey)
                                          .orElse("-");

        return new InsightCardEntity(
                "Most visited location",
                topLocationName,
                InsightType.LOCATION
        );
    }

    private String getTimeLabel(Resolution resolution) {
        return switch (resolution) {
            case WEEK -> "this week";
            case MONTH -> "this month";
            case DAILY -> "today";
            case YEAR -> "this year";
        };
    }

}
