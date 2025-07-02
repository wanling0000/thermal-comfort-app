package com.wanling.test.domain.environmental.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.wanling.domain.environmental.model.entity.DailyComfortStatEntity;
import com.wanling.domain.environmental.model.entity.SummaryInsightEntity;
import com.wanling.domain.environmental.model.entity.YearlyComfortStatsEntity;
import com.wanling.domain.environmental.model.valobj.DailyChartPoint;
import com.wanling.domain.environmental.service.IAnalyticsService;
import com.wanling.types.enums.Resolution;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AnalyticsServiceTest {
    @Resource
    private IAnalyticsService analyticsService;

    @Test
    void shouldReturnDailyChartPointsForAdmin() {
        String userId = "admin";
        LocalDate testDate = LocalDate.of(2025, 5, 26);

        List<DailyChartPoint> points = analyticsService.queryDailyView(userId, testDate);

        log.info("✅ Fetched {} daily chart points", points.size());

        for (DailyChartPoint point : points) {
            log.info("🕒 Time={} | 🌡️ Temp={} | 💧 Humid={} | Feedback={}",
                    point.timestamp(),
                    point.temperature(),
                    point.humidity(),
                    point.feedback().map(Object::toString).orElse("-"));
        }

        assertNotNull(points);
        assertFalse(points.isEmpty(), "Expected at least one data point.");
    }

    @Test
    void shouldReturnFullYearlyStats() {
        String userId = "admin";
        int year = 2025;

        YearlyComfortStatsEntity yearlyStats = analyticsService.getYearlyStats(userId, year);
        List<DailyComfortStatEntity> dailyStats = yearlyStats.data();

        assertEquals(365, dailyStats.size(), "✅ 应该有 365 天的数据");

        long withData = dailyStats.stream()
                                  .filter(s -> s.averageComfort().isPresent())
                                  .count();

        long withoutData = dailyStats.stream()
                                     .filter(s -> s.averageComfort().isEmpty())
                                     .count();

        log.info("📊 总天数={} | 有记录={} | 无记录={}", dailyStats.size(), withData, withoutData);

        // 检查某个具体日期有数据（假设你知道这天有）
        LocalDate expectedDate = LocalDate.of(2025, 5, 26);
        dailyStats.stream()
                  .filter(d -> d.date().equals(expectedDate))
                  .findFirst()
                  .ifPresentOrElse(stat -> {
                      log.info("✅ {} 的平均舒适度为: {}", expectedDate, stat.averageComfort());
                      assertTrue(stat.averageComfort().isPresent());
                      assertTrue(stat.feedbackCount() > 0);
                  }, () -> fail("❌ 没有找到 " + expectedDate + " 的统计"));

        // 检查某个无记录的日期
        LocalDate emptyDate = LocalDate.of(2025, 1, 1);
        dailyStats.stream()
                  .filter(d -> d.date().equals(emptyDate))
                  .findFirst()
                  .ifPresent(stat -> {
                      log.info("📭 {} 无数据，平均舒适度为: {}", emptyDate, stat.averageComfort());
                      assertTrue(stat.averageComfort().isEmpty());
                      assertEquals(0, stat.feedbackCount());
                  });
    }

    @Test
    public void testBuildDailySummaryViaPublicInterface() {
        String userId = "admin";
        LocalDate date = LocalDate.of(2025, 5, 26);

        SummaryInsightEntity result = analyticsService.getSummaryInsights(userId, date, Resolution.DAILY);

        assertNotNull(result);
        System.out.println("🟩 Summary for " + date + ":");
        result.insights().forEach(card ->
                System.out.println("📌 " + card.title() + ": " + card.content())
        );
    }

    @Test
    public void testBuildWeeklySummaryViaPublicInterface() {
        String userId = "admin";
        LocalDate date = LocalDate.of(2025, 5, 26); // 任意在一周中间的一天

        SummaryInsightEntity result = analyticsService.getSummaryInsights(userId, date, Resolution.WEEK);

        assertNotNull(result);
        System.out.println("🟦 Weekly Summary from " + result.startDate() + " to " + result.endDate() + ":");
        result.insights().forEach(card ->
                System.out.println("📌 " + card.title() + ": " + card.content())
        );
        result.locationInsights().forEach(loc ->
                System.out.println("📍 " + loc.locationName() + " | Total: " + loc.comfortStats().total() + ", Comfortable: " + loc.comfortStats().countComfort())
        );
    }

    @Test
    public void testBuildMonthlySummaryViaPublicInterface() {
        String userId = "admin";
        LocalDate date = LocalDate.of(2025, 5, 1); // 月内任意一天即可

        SummaryInsightEntity result = analyticsService.getSummaryInsights(userId, date, Resolution.MONTH);

        assertNotNull(result);
        System.out.println("🟨 Monthly Summary from " + result.startDate() + " to " + result.endDate() + ":");
        result.insights().forEach(card ->
                System.out.println("📌 " + card.title() + ": " + card.content())
        );
        result.locationInsights().forEach(loc ->
                System.out.println("📍 " + loc.locationName() + " | Total: " + loc.comfortStats().total() + ", Comfortable: " + loc.comfortStats().countComfort())
        );
    }


    @Test
    public void testBuildYearlySummaryViaPublicInterface() {
        String userId = "admin";
        LocalDate date = LocalDate.of(2025, 1, 1); // 测试2025年

        SummaryInsightEntity result = analyticsService.getSummaryInsights(userId, date, Resolution.YEAR);

        assertNotNull(result);
        System.out.println("📅 Yearly Summary for " + date.getYear() + ":");

        // 打印 insight 卡片
        result.insights().forEach(card ->
                System.out.println("📌 " + card.title() + ": " + card.content())
        );

        // 打印 location insight（如果有）
        if (!result.locationInsights().isEmpty()) {
            System.out.println("📍 Location stats:");
            result.locationInsights().forEach(loc -> {
                System.out.printf(
                        "- %s (%.4f, %.4f): %d feedbacks, Comfortable: %d\n",
                        loc.locationName(),
                        loc.latitude(),
                        loc.longitude(),
                        loc.comfortStats().total(),
                        loc.comfortStats().countComfort()
                );
            });
        } else {
            System.out.println("📍 No location insight this year.");
        }
    }

}