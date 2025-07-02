package com.wanling.trigger.api.dto;

import lombok.Builder;

@Builder
public record LocationPreviewDTO(
    String displayName,
    boolean isCustom,
    String customTag,
    double latitude,
    double longitude
) {}
