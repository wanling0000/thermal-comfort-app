package com.wanling.domain.environmental.model.entity;

import com.wanling.types.enums.InsightType;

public record InsightCardEntity(
        String title,// 卡片标题（如 “Most Frequent Activity”）
        String content,// 内容文本（如 “You were mostly sitting”）
        InsightType type// 可选: TEMPERATURE_RANGE / ACTIVITY / COMPARISON 等
) {
}
