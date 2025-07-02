package com.wanling.domain.environmental.model.entity;

import java.util.Map;

public record ComfortStatisticsEntity(
        int total,
        int countTooCold,   // -2
        int countCold,      // -1
        int countComfort,   // 0
        int countWarm,      // 1
        int countTooHot     // 2
) {
    public Map<Integer, Integer> toLevelCountMap() {
        return Map.of(
                -2, countTooCold,
                -1, countCold,
                0, countComfort,
                1, countWarm,
                2, countTooHot
        );
    }
}
