package com.wanling.test.infrastructure.persistent.repository;

import java.util.List;

import javax.annotation.Resource;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.repository.IComfortFeedbackRepository;
import com.wanling.trigger.api.dto.ComfortFeedbackResponseDTO;
import com.wanling.trigger.assembler.ComfortFeedbackAssembler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
@SpringBootTest
class ComfortFeedbackRepositoryTest {
    @Resource
    private IComfortFeedbackRepository repository;
    @Test
    void shouldPrintAllFeedbackEntities() {
        String userId = "admin";
        List<ComfortFeedbackEntity> list = repository.findAllByUserIdOrderByTimestampDesc(userId);

        for (ComfortFeedbackEntity e : list) {
            log.info("""
                📝 FeedbackEntity {
                    feedbackId        = {}
                    userId            = {}
                    timestamp         = {}
                    comfortLevel      = {}
                    feedbackType      = {}
                    activityTypeId    = {}
                    clothingLevel     = {}
                    adjustedTempLevel = {}
                    adjustedHumidLevel= {}
                    notes             = {}
                    locationTagId     = {}
                    userLocationTagId = {}
                    locationDisplayName = {}
                    isCustomLocation  = {}
                    customTagName     = {}
                    readingId         = {}
                    rawLatitude       = {}
                    rawLongitude      = {}
                }""",
                    e.getFeedbackId(),
                    e.getUserId(),
                    e.getTimestamp(),
                    e.getComfortLevel(),
                    e.getFeedbackType(),
                    e.getActivityTypeId().orElse(""),
                    e.getClothingLevel().orElse(""),
                    e.getAdjustedTempLevel().map(String::valueOf).orElse(""),
                    e.getAdjustedHumidLevel().map(String::valueOf).orElse(""),
                    e.getNotes().orElse(""),
                    e.getLocationTagId(),
                    e.getUserLocationTagId().orElse(""),
                    e.getLocationDisplayName(),
                    e.isCustomLocation(),
                    e.getCustomTagName().orElse(""),
                    e.getReadingId(),
                    e.getRawLatitude(),
                    e.getRawLongitude()
            );
        }

        assertFalse(list.isEmpty(), "反馈列表不应为空");
    }
}