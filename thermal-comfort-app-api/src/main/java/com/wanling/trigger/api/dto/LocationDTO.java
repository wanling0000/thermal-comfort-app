package com.wanling.trigger.api.dto;

public record LocationDTO(
        String displayName,
        Boolean isCustom,
        String customTag,
        Double latitude,
        Double longitude
) {}
