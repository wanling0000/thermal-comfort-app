package com.wanling.domain.environmental.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationTagEntity {
    private String locationTagId;
    private String displayName;
    private Double latitude;
    private Double longitude;
    private Boolean isCustom;
    private String createdAt;
}
