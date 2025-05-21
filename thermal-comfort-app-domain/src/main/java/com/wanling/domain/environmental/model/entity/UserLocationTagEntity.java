package com.wanling.domain.environmental.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationTagEntity {
    private String userLocationTagId;
    private String userId;
    private String name;
    private Double latitude; // Optional
    private Double longitude; // Optional
    private String note;
    private String relatedLocationTagId;
    private String createdAt;
}
