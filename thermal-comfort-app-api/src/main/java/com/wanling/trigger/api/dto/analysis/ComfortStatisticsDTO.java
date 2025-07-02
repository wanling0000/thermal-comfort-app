package com.wanling.trigger.api.dto.analysis;

import java.util.Map;

public record ComfortStatisticsDTO(
        int total,
        Map<Integer, Integer> levelCounts// key: -2 ~ 2, value: count
) {}

