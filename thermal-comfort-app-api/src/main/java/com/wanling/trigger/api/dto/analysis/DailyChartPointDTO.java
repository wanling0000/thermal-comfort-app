package com.wanling.trigger.api.dto.analysis;

import com.wanling.trigger.api.dto.FeedbackInfoDTO;

public record DailyChartPointDTO (
        long timestamp,
        double temperature,
        double humidity,
        FeedbackInfoDTO feedback) {
}
