package com.wanling.domain.environmental.model.entity;

public record LocationInsightEntity(
        String locationName,
        double latitude,
        double longitude,
        ComfortStatisticsEntity comfortStats    // 总反馈数、舒适比例等
) {
}
