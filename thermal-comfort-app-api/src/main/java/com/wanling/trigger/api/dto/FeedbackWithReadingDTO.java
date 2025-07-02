package com.wanling.trigger.api.dto;

public record FeedbackWithReadingDTO(
        ComfortFeedbackDTO feedback,
        EnvironmentalReadingDTO reading
) {}

