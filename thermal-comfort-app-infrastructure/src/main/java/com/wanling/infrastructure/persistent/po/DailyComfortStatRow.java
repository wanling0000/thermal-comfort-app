package com.wanling.infrastructure.persistent.po;

import java.time.LocalDate;

public record DailyComfortStatRow(
        LocalDate date, Double averageComfort, int feedbackCount) {}