package com.wanling.domain.environmental.model.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ComfortFeedbackEntity {
    private String feedbackId;
    private String userId;
    private LocalDateTime timestamp;
    private int comfortLevel;
    private String feedbackType;

    // optional fields
    private Optional<String> activityTypeId;
    private Optional<String> clothingLevel;
    private Optional<Integer> adjustedTempLevel;
    private Optional<Integer> adjustedHumidLevel;
    private Optional<String> notes;

    // location info
    private String locationDisplayName;
    private boolean isCustomLocation;
    private Optional<String> customTagName;

    private String locationTagId;
    private Optional<String> userLocationTagId;

    private String readingId;
    private double rawLatitude;
    private double rawLongitude;
}