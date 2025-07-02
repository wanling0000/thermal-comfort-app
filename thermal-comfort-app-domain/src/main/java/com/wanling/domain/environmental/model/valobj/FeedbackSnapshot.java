package com.wanling.domain.environmental.model.valobj;

import java.util.Optional;

public record FeedbackSnapshot(
    String feedbackId,
    int comfortLevel,
    Optional<String> notes,
    Optional<String> activityTypeId,
    Optional<String> clothingLevel,
    String readingId
) {}