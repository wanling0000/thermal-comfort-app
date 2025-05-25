package com.wanling.trigger.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public record LocationDTO(
        String displayName,
        Boolean isCustom,
        String customTag,
        Double latitude,
        Double longitude
) {}
