package com.wanling.trigger.api.dto.analysis;

public record LocationInsightDTO(
    String locationName,
    double latitude,
    double longitude,
    ComfortStatisticsDTO comfortStats    // 总反馈数、舒适比例等
) {}