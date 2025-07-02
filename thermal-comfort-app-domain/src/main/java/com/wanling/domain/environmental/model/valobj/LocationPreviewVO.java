package com.wanling.domain.environmental.model.valobj;

public record LocationPreviewVO(
    String displayName,
    boolean isCustom,
    String customTag,
    Double latitude,
    Double longitude
) {}