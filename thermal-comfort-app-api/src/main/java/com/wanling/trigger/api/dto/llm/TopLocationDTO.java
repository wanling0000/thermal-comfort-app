package com.wanling.trigger.api.dto.llm;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopLocationDTO {
    private String name;
    private double latitude;
    private double longitude;

    private Map<Integer, Integer> comfortStats; // -2 ~ 2 -> count
    private int totalCount;
}