package com.wanling.domain.environmental.repository;

import java.util.List;

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
}
