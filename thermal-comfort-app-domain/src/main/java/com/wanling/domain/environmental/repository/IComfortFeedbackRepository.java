package com.wanling.domain.environmental.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.entity.DailyComfortStatEntity;
import com.wanling.domain.environmental.model.valobj.FeedbackSnapshot;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 22/05/2025
 * 12:32
 */
public interface IComfortFeedbackRepository {
    void saveFeedback(ComfortFeedbackEntity entity);
    List<ComfortFeedbackEntity> findAllByUserIdOrderByTimestampDesc(String userId);

    Optional<ComfortFeedbackEntity> findLatestByUserId(String userId);

    List<FeedbackSnapshot> findSnapshotsByUserIdAndTimeRange(String userId, LocalDateTime from, LocalDateTime to);

    List<DailyComfortStatEntity> findDailyStatsForUserInYear(String userId, LocalDate start, LocalDate end);

    List<ComfortFeedbackEntity> findByUserAndDate(String userId, LocalDate date);

    List<ComfortFeedbackEntity> findByUserAndDateRange(String userId, LocalDate start, LocalDate end);
}
