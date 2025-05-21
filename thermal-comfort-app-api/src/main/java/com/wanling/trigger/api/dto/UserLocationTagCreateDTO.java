package com.wanling.trigger.api.dto;

import lombok.Data;

@Data
public class UserLocationTagCreateDTO {
    private String name; // Required
    private LocationDTO location; // Optional (may be null)
}
