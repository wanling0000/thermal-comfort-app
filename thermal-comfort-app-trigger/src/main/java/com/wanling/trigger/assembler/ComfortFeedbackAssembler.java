package com.wanling.trigger.assembler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.trigger.api.dto.ComfortFeedbackDTO;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 22/05/2025
 * 11:53
 */
public class ComfortFeedbackAssembler {
    public static ComfortFeedbackEntity toEntity(ComfortFeedbackDTO dto) {
        return ComfortFeedbackEntity.builder()
                                    .feedbackId(null) // set later
                                    .userId(null)     // set later from context
                                    .timestamp(LocalDateTime.ofInstant(
                                            Instant.ofEpochMilli(dto.getTimestamp()), ZoneId.systemDefault()))
                                    .comfortLevel(dto.getComfortLevel())
                                    .feedbackType(dto.getFeedbackType())
                                    .activityTypeId(Optional.ofNullable(dto.getActivityTypeId()))
                                    .clothingLevel(Optional.ofNullable(dto.getClothingLevel()))
                                    .adjustedTempLevel(Optional.ofNullable(dto.getAdjustedTempLevel()))
                                    .adjustedHumidLevel(Optional.ofNullable(dto.getAdjustedHumidLevel()))
                                    .notes(Optional.ofNullable(dto.getNotes()))
                                    .customTagName(Optional.ofNullable(dto.getCustomTagName()))
                                    .locationTagId(null)
                                    .userLocationTagId(Optional.empty())
                                    .readingId(null)
                                    .rawLatitude(dto.getRawCoordinates().getLatitude())
                                    .rawLongitude(dto.getRawCoordinates().getLongitude())
                                    .build();
    }
}
