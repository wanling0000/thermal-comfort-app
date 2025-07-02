package com.wanling.trigger.api.dto;

public record FeedbackInfoDTO(
        String feedbackId,
        Integer comfortLevel,
        java.util.Optional<String> notes,
        java.util.Optional<String> activityTypeId,
        java.util.Optional<String> clothingLevel
) {}
