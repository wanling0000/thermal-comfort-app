package com.wanling.trigger.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ComfortFeedbackWithIdDTO(
    String feedbackId,
    @JsonProperty("comfort_level") int comfortLevel,
    @JsonProperty("feedback_type") String feedbackType,
    long timestamp,
    String locationDisplayName,
    Boolean isCustomLocation,
    String customTagName,
    @JsonProperty("raw_coordinates") RawCoordinate rawCoordinates,
    String notes,
    String activityTypeId,
    String clothingLevel,
    Integer adjustedTempLevel,
    Integer adjustedHumidLevel
) {}
