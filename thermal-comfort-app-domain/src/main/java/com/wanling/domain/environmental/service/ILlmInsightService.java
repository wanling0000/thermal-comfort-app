package com.wanling.domain.environmental.service;

import java.time.LocalDate;

import com.wanling.domain.environmental.model.entity.llm.MonthlyLLMInsightEntity;

public interface ILlmInsightService {
    MonthlyLLMInsightEntity generateMonthlyInsight(String userId, LocalDate date);
}