package com.wanling.trigger.api.dto;

import lombok.Data;

@Data
public class LocationDTO {
    private String displayName;
    private Boolean isCustom;
    private String customTag;
    private Double latitude;
    private Double longitude;
}
