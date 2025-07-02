package com.wanling.domain.environmental.model.entity;

import java.time.LocalDate;
import java.util.Optional;

public record DailyComfortStatEntity(
        LocalDate date,
        Optional<Double> averageComfort,
        int feedbackCount
){

}
