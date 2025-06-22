package com.wanling.trigger.api.dto;

import lombok.Data;

public record FeedbackWithReadingDTO(
        ComfortFeedbackDTO feedback,
        EnvironmentalReadingDTO reading
) {}

