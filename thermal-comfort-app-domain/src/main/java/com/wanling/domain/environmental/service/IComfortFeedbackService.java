package com.wanling.domain.environmental.service;

import java.util.List;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;

public interface IComfortFeedbackService {
    void handleFeedbackWithReading(ComfortFeedbackEntity feedback, EnvironmentalReadingEntity reading);
    List<ComfortFeedbackEntity> getAllFeedback();

    ComfortFeedbackEntity findLatestFeedbackForCurrentUser();
}