package com.wanling.trigger.http;

import java.time.LocalDate;

import com.wanling.domain.environmental.model.entity.llm.MonthlyLLMInsightEntity;
import com.wanling.domain.environmental.service.ILlmInsightService;
import com.wanling.trigger.api.dto.llm.MonthlyLLMInsightDTO;
import com.wanling.trigger.assembler.LlmAssembler;
import com.wanling.trigger.assembler.LoginAssembler;
import com.wanling.types.enums.ResponseCode;
import com.wanling.types.model.Response;
import com.wanling.types.security.LoginUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Slf4j
public class LlmInsightController {

    private final ILlmInsightService llmInsightService;

    @GetMapping("/insight/month")
    public Response<MonthlyLLMInsightDTO> getMonthlyLLMInsight(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String userId = LoginUserHolder.get().userId();
        log.info("🧠 Request monthly AI insight for user={}, date={}", userId, date);

        MonthlyLLMInsightEntity entity = llmInsightService.generateMonthlyInsight(userId, date);
        MonthlyLLMInsightDTO dto = LlmAssembler.toMonthlyLLMInsightDTO(entity);

        return Response.<MonthlyLLMInsightDTO>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("AI monthly insight ready")
                       .data(dto)
                       .build();
    }
}