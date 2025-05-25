package com.wanling.trigger.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class ComfortFeedbackDTO {
    @JsonProperty("comfort_level")
    private int comfortLevel;
    @JsonProperty("feedback_type")
    private String feedbackType;
    private long timestamp;

    private String locationDisplayName;
    private Boolean isCustomLocation;
    private String customTagName;

    @JsonProperty("raw_coordinates")
    private RawCoordinate rawCoordinates;

    private String notes;
    private String activityTypeId;
    private String clothingLevel;
    private Integer adjustedTempLevel;
    private Integer adjustedHumidLevel;
}


