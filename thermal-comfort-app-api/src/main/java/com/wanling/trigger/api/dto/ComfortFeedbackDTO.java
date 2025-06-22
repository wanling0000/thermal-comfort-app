package com.wanling.trigger.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ComfortFeedbackDTO(
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
) {
    @JsonCreator
    public ComfortFeedbackDTO {
        // record 构造器，只要字段名和参数名一致就不用写逻辑
    }
}

