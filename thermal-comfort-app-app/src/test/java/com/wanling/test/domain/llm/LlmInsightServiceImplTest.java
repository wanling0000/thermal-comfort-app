package com.wanling.test.domain.llm;

import java.time.LocalDate;

import com.wanling.domain.environmental.model.entity.llm.MonthlyLLMInsightEntity;
import com.wanling.domain.environmental.service.ILlmInsightService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class LlmInsightServiceImplTest {

    @Autowired
    private ILlmInsightService llmInsightService;
    @Test
    public void testGenerateMonthlyInsight() {
        String userId = "8d468f7b-03f6-4760-a4b8-220e175b232c";
        LocalDate date = LocalDate.of(2025, 7, 1);

        MonthlyLLMInsightEntity result = llmInsightService.generateMonthlyInsight(userId, date);

        log.info("🧠 Insight Summary:\n{}", result);
    }
}