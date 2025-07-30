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
        String userId = "8d468f7b-03f6-4760-a4b8-220e175b232c";
        LocalDate testDate = LocalDate.of(2025, 7, 5);

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
        String userId = "8d468f7b-03f6-4760-a4b8-220e175b232c";
        int year = 2025;

        YearlyComfortStatsEntity yearlyStats = analyticsService.getYearlyStats(userId, year);
        List<DailyComfortStatEntity> dailyStats = yearlyStats.data();

        assertEquals(365, dailyStats.size(), "✅ Should have data for 365 days");

        long withData = dailyStats.stream()
                                  .filter(s -> s.averageComfort().isPresent())
                                  .count();

        long withoutData = dailyStats.stream()
                                     .filter(s -> s.averageComfort().isEmpty())
                                     .count();

        log.info("📊 Total Days={} | With Data={} | Without Data={}", dailyStats.size(), withData, withoutData);

        // Check that a specific day has data (assuming we know this date has records)
        LocalDate expectedDate = LocalDate.of(2025, 5, 26);
        dailyStats.stream()
                  .filter(d -> d.date().equals(expectedDate))
                  .findFirst()
                  .ifPresentOrElse(stat -> {
                      log.info("✅ Average comfort for {} is: {}", expectedDate, stat.averageComfort());
                      assertTrue(stat.averageComfort().isPresent());
                      assertTrue(stat.feedbackCount() > 0);
                  }, () -> fail("❌ No stats found for " + expectedDate));

        // Check a date without data
        LocalDate emptyDate = LocalDate.of(2025, 1, 1);
        dailyStats.stream()
                  .filter(d -> d.date().equals(emptyDate))
                  .findFirst()
                  .ifPresent(stat -> {
                      log.info("📭 No data for {}, average comfort: {}", emptyDate, stat.averageComfort());
                      assertTrue(stat.averageComfort().isEmpty());
                      assertEquals(0, stat.feedbackCount());
                  });
    }

    @Test
    public void testBuildDailySummaryViaPublicInterface() {
        String userId = "8d468f7b-03f6-4760-a4b8-220e175b232c";
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
        String userId = "8d468f7b-03f6-4760-a4b8-220e175b232c";
        LocalDate date = LocalDate.of(2025, 7, 5); // Any day in the middle of a week

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
        String userId = "8d468f7b-03f6-4760-a4b8-220e175b232c";
        LocalDate date = LocalDate.of(2025, 5, 1); // Any day in the month

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
        String userId = "8d468f7b-03f6-4760-a4b8-220e175b232c";
        LocalDate date = LocalDate.of(2025, 1, 1); // Test year 2025

        SummaryInsightEntity result = analyticsService.getSummaryInsights(userId, date, Resolution.YEAR);

        assertNotNull(result);
        System.out.println("📅 Yearly Summary for " + date.getYear() + ":");

        // Print insight cards
        result.insights().forEach(card ->
                System.out.println("📌 " + card.title() + ": " + card.content())
        );

        // Print location insights (if any)
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
