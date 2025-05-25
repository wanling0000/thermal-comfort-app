package com.wanling.trigger.http;

import com.alibaba.fastjson.JSON;
import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.service.IComfortFeedbackService;
import com.wanling.trigger.api.dto.ComfortFeedbackDTO;
import com.wanling.trigger.api.dto.FeedbackWithReadingDTO;
import com.wanling.trigger.api.dto.LocationDTO;
import com.wanling.trigger.assembler.ComfortFeedbackAssembler;
import com.wanling.trigger.assembler.EnvironmentalReadingAssembler;
import com.wanling.types.enums.ResponseCode;
import com.wanling.types.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 22/05/2025
 * 11:33
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/feedback")
public class ComfortFeedbackController {
    private final IComfortFeedbackService comfortFeedbackService;

    @PostMapping("/submit-with-reading")
    public Response<String> submitFeedback(@RequestBody FeedbackWithReadingDTO dto) {
        LocationDTO loc = dto.getReading().location();
        System.out.println("👀 displayName = " + loc.displayName());
        log.info("✅ Received quick feedback: {}", JSON.toJSONString(dto));

        EnvironmentalReadingEntity readingEntity = EnvironmentalReadingAssembler.toEntity(dto.getReading());
        ComfortFeedbackEntity feedbackEntity = ComfortFeedbackAssembler.toEntity(dto.getFeedback());

        log.info("📦 readingEntity = {}", JSON.toJSONString(readingEntity));
        log.info("📦 feedbackEntity = {}", JSON.toJSONString(feedbackEntity));

        comfortFeedbackService.handleFeedbackWithReading(feedbackEntity, readingEntity);

        return Response.<String>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("Feedback received")
                       .data("OK")
                       .build();
    }
}
