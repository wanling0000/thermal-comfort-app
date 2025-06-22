package com.wanling.domain.environmental.repository;

import java.util.List;
import java.util.Optional;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;

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
}
