package com.wanling.trigger.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ComfortFeedbackResponseDTO(
        @JsonProperty("comfort_level") int comfortLevel,
        @JsonProperty("feedback_type") String feedbackType,
        long timestamp,
        @JsonProperty("location_display_name") String locationDisplayName,
        @JsonProperty("is_custom_location") Boolean isCustomLocation,
        @JsonProperty("custom_tag_name") String customTagName,
        @JsonProperty("raw_coordinates") RawCoordinate rawCoordinates,
        String notes,
        @JsonProperty("activity_type_id") String activityTypeId,
        @JsonProperty("clothing_level") String clothingLevel,
        @JsonProperty("adjusted_temp_level") Integer adjustedTempLevel,
        @JsonProperty("adjusted_humid_level") Integer adjustedHumidLevel
) {}

