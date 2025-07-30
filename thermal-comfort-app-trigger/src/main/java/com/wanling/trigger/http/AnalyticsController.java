package com.wanling.trigger.http;

import java.time.LocalDate;
import java.util.List;

import com.wanling.domain.environmental.model.entity.ComfortStatisticsEntity;
import com.wanling.domain.environmental.model.entity.SummaryInsightEntity;
import com.wanling.domain.environmental.model.entity.YearlyComfortStatsEntity;
import com.wanling.domain.environmental.model.valobj.DailyChartPoint;
import com.wanling.domain.environmental.service.IAnalyticsService;
import com.wanling.trigger.api.dto.analysis.ComfortStatisticsDTO;
import com.wanling.trigger.api.dto.analysis.DailyChartPointDTO;
import com.wanling.trigger.api.dto.analysis.SummaryInsightResponseDTO;
import com.wanling.trigger.api.dto.analysis.YearlyComfortStatsDTO;
import com.wanling.trigger.assembler.AnalyticsAssembler;
import com.wanling.types.enums.Resolution;
import com.wanling.types.enums.ResponseCode;
import com.wanling.types.model.Response;
import com.wanling.types.security.LoginUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {

    private final IAnalyticsService analyticsService;

    @GetMapping("/daily")
    public Response<List<DailyChartPointDTO>> getDailyAnalytics(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String userId = LoginUserHolder.get().userId();
        log.debug("📊 Request daily analytics for user={}, date={}", userId, date);

        List<DailyChartPoint> voList = analyticsService.queryDailyView(userId, date);
        List<DailyChartPointDTO> dtoList = voList.stream()
                                                 .map(AnalyticsAssembler::toDailyChartPointDTO)
                                                 .toList();

        return Response.<List<DailyChartPointDTO>>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("Fetched " + dtoList.size() + " points")
                       .data(dtoList)
                       .build();
    }

    @GetMapping("/week")
    public Response<ComfortStatisticsDTO> getWeeklyComfortStats(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String userId = LoginUserHolder.get().userId();
        ComfortStatisticsEntity entity = analyticsService.getComfortStatistics(userId, date, Resolution.WEEK);
        ComfortStatisticsDTO dto = AnalyticsAssembler.toComfortStatisticsDTO(entity);
        return Response.<ComfortStatisticsDTO>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("OK")
                       .data(dto)
                       .build();
    }

    @GetMapping("/month")
    public Response<ComfortStatisticsDTO> getMonthlyComfortStats(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String userId = LoginUserHolder.get().userId();
        ComfortStatisticsEntity entity = analyticsService.getComfortStatistics(userId, date, Resolution.MONTH);
        ComfortStatisticsDTO dto = AnalyticsAssembler.toComfortStatisticsDTO(entity);
        return Response.<ComfortStatisticsDTO>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("OK")
                       .data(dto)
                       .build();
    }

    @GetMapping("/yearly")
    public Response<YearlyComfortStatsDTO> getYearlyStats(
            @RequestParam int year
    ) {
        String userId = LoginUserHolder.get().userId();
        YearlyComfortStatsEntity entity = analyticsService.getYearlyStats(userId, year);
        YearlyComfortStatsDTO dto = AnalyticsAssembler.toYearlyComfortStatsDTO(entity);
        return Response.<YearlyComfortStatsDTO>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("OK")
                       .data(dto)
                       .build();
    }

    @GetMapping("/summary")
    public Response<SummaryInsightResponseDTO> getSummaryInsights(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Resolution resolution
    ) {
        String userId = LoginUserHolder.get().userId();
        SummaryInsightEntity entity = analyticsService.getSummaryInsights(userId, date, resolution);
        SummaryInsightResponseDTO dto = AnalyticsAssembler.toSummaryInsightDTO(entity);
        return Response.<SummaryInsightResponseDTO>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("OK")
                       .data(dto)
                       .build();
    }

}
