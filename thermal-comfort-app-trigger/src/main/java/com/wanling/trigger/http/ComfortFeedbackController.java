package com.wanling.trigger.http;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.service.IComfortFeedbackService;
import com.wanling.trigger.api.dto.ComfortFeedbackResponseDTO;
import com.wanling.trigger.api.dto.FeedbackWithReadingDTO;
import com.wanling.trigger.api.dto.LocationDTO;
import com.wanling.trigger.assembler.ComfortFeedbackAssembler;
import com.wanling.trigger.assembler.EnvironmentalReadingAssembler;
import com.wanling.types.enums.ResponseCode;
import com.wanling.types.model.Response;
import com.wanling.types.security.LoginUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        LocationDTO loc = dto.reading().location();
        System.out.println("👀 displayName = " + loc.displayName());
        log.info("✅ Received feedback: {}", JSON.toJSONString(dto));

        String userId = LoginUserHolder.get().userId();
        EnvironmentalReadingEntity readingEntity = EnvironmentalReadingAssembler.toEntity(dto.reading(), userId);
        ComfortFeedbackEntity feedbackEntity = ComfortFeedbackAssembler.toEntity(dto.feedback());

        log.info("📦 readingEntity = {}", JSON.toJSONString(readingEntity));
        log.info("📦 feedbackEntity = {}", JSON.toJSONString(feedbackEntity));

        comfortFeedbackService.handleFeedbackWithReading(feedbackEntity, readingEntity);

        return Response.<String>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("Feedback received")
                       .data("OK")
                       .build();
    }

    @GetMapping("/by-month")
    public Response<List<ComfortFeedbackResponseDTO>> getAllFeedbackByMonth(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        String userId = LoginUserHolder.get().userId();
        List<ComfortFeedbackEntity> entities = comfortFeedbackService.getFeedbackByMonth(year, month, userId);

        List<ComfortFeedbackResponseDTO> dtos = entities.stream()
                                                           .map((ComfortFeedbackEntity entity) -> ComfortFeedbackAssembler.toResponseDTO(entity))
                                                        .toList();

        return Response.<List<ComfortFeedbackResponseDTO>>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("getFeedbackByMonth(year, month);() success")
                       .data(dtos)
                       .build();
    }

    @GetMapping("/latest")
    public Response<ComfortFeedbackResponseDTO> getLatestFeedback() {
        String userId = LoginUserHolder.get().userId();
        ComfortFeedbackEntity latest = comfortFeedbackService.findLatestFeedbackForCurrentUser(userId);
        ComfortFeedbackResponseDTO dto = ComfortFeedbackAssembler.toResponseDTO(latest);
        return Response.<ComfortFeedbackResponseDTO>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("OK")
                       .data(dto)
                       .build();
    }

    @DeleteMapping("/delete/{id}")
    public Response<String> deleteFeedback(@PathVariable("id") String id) {
        String userId = LoginUserHolder.get().userId();
        log.info("🗑️ Deleting feedback: id = {}, userId = {}", id, userId);

        comfortFeedbackService.deleteFeedback(id, userId);

        return Response.<String>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("Feedback deleted")
                       .data("OK")
                       .build();
    }

    @PutMapping("/update")
    public Response<String> updateFeedback(@RequestBody ComfortFeedbackResponseDTO dto) {
        try {
            String userId = LoginUserHolder.get().userId();
            log.info("✏️ Updating feedback: userId = {}, data = {}", userId, JSON.toJSONString(dto));

            ComfortFeedbackEntity partialEntity = ComfortFeedbackAssembler.toPartialEntityForUpdate(dto, userId);
            comfortFeedbackService.updateFeedback(partialEntity);

            return Response.<String>builder()
                           .code(ResponseCode.SUCCESS.getCode())
                           .info("Feedback updated")
                           .data("OK")
                           .build();
        } catch (Exception e) {
            log.error("❌ Exception in updateFeedback()", e);
            throw e;
        }
    }
}
