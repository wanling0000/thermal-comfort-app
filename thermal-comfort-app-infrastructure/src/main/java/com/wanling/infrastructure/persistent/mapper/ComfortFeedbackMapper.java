package com.wanling.infrastructure.persistent.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.infrastructure.persistent.po.ComfortFeedback;
import com.wanling.infrastructure.persistent.po.DailyComfortStatRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author fwl
* @description 针对表【comfort_feedback】的数据库操作Mapper
* @createDate 2025-05-22 11:58:19
* @Entity com.wanling.infrastructure.persistent.po.ComfortFeedback
*/

@Mapper
public interface ComfortFeedbackMapper {

    int deleteByPrimaryKey(String id);

    int insert(ComfortFeedback record);

    int insertSelective(ComfortFeedback record);

    ComfortFeedback selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ComfortFeedback record);

    int updateByPrimaryKey(ComfortFeedback record);

    List<ComfortFeedback> selectMissingReadingId();

    void updateReadingId(
            @Param("feedbackId") String feedbackId,
            @Param("readingId") String readingId
    );

    List<ComfortFeedback> findAllByUserIdOrderByTimestampDesc(@Param("userId") String userId);

    ComfortFeedback findLatestByUserId(String userId);

    List<ComfortFeedback> findByUserIdAndTimeRange(
            @Param("userId") String userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    List<DailyComfortStatRow> findDailyStatsForUserInYear(
            @Param("userId") String userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<ComfortFeedback> selectByUserIdAndDate(
            @Param("userId") String userId,
            @Param("date") LocalDate date
    );

    List<ComfortFeedback> selectByUserIdAndDateRange(
            @Param("userId") String userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    void markAsDeleted(@Param("feedbackId") String feedbackId, @Param("userId") String userId);

    ComfortFeedback selectByIdAndNotDeleted(@Param("id") String id);
}
