package com.wanling.domain.environmental.model.valobj;

import java.util.Optional;

public record DailyChartPoint(
    long timestamp,
    double temperature,
    double humidity,
    Optional<FeedbackSnapshot> feedback
) {}
