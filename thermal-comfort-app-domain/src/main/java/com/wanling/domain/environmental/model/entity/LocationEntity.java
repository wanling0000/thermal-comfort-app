package com.wanling.domain.environmental.model.entity;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {
    private String locationId;
    private String name;
    private String description;
    private Boolean isCustom;
    private String customTag;
    private String coordinates;
    private String userId;
}