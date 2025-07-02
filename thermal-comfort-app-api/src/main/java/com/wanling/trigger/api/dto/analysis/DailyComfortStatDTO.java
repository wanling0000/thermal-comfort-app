package com.wanling.trigger.api.dto.analysis;

public record DailyComfortStatDTO(
        String date,                // ISO 格式字符串（如 "2025-01-01"）
        Double averageComfort,      // 可以为 null
        int feedbackCount
) {}
