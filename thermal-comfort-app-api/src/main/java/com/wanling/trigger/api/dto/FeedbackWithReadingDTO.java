package com.wanling.trigger.api.dto;

import lombok.Data;

@Data
public class FeedbackWithReadingDTO {
    private ComfortFeedbackDTO feedback;
    private EnvironmentalReadingDTO reading;
}
