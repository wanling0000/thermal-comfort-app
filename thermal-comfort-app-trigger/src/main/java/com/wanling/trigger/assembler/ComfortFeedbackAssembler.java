package com.wanling.trigger.assembler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.valobj.ComfortFeedbackWithReadingVO;
import com.wanling.trigger.api.dto.ComfortFeedbackDTO;
import com.wanling.trigger.api.dto.ComfortFeedbackResponseDTO;
import com.wanling.trigger.api.dto.ComfortFeedbackWithReadingDTO;
import com.wanling.trigger.api.dto.RawCoordinate;
import com.wanling.types.security.LoginUserHolder;
import org.postgresql.util.PGobject;

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
                                    .userId(LoginUserHolder.get().userId())     // set later from context
                                    .timestamp(LocalDateTime.ofInstant(
                                            Instant.ofEpochMilli(dto.timestamp()), ZoneId.systemDefault()))
                                    .comfortLevel(dto.comfortLevel())
                                    .feedbackType(dto.feedbackType())
                                    .activityTypeId(Optional.ofNullable(dto.activityTypeId()))
                                    .clothingLevel(Optional.ofNullable(dto.clothingLevel()))
                                    .adjustedTempLevel(Optional.ofNullable(dto.adjustedTempLevel()))
                                    .adjustedHumidLevel(Optional.ofNullable(dto.adjustedHumidLevel()))
                                    .notes(Optional.ofNullable(dto.notes()))
                                    .customTagName(Optional.ofNullable(dto.customTagName()))
                                    .locationTagId(null)
                                    .userLocationTagId(Optional.empty())
                                    .readingId(null)
                                    .rawLatitude(Optional.of(dto.rawCoordinates().latitude()))
                                    .rawLongitude(Optional.of(dto.rawCoordinates().longitude()))
                                    .build();
    }

    public static ComfortFeedbackResponseDTO toResponseDTO(ComfortFeedbackEntity entity) {
        return ComfortFeedbackResponseDTO.builder()
                                         .feedbackId(entity.getFeedbackId())
                                         .comfortLevel(entity.getComfortLevel())
                                         .feedbackType(entity.getFeedbackType())
                                         .timestamp(entity.getTimestamp()
                                                          .atZone(ZoneId.systemDefault()) // ← 这里用系统默认时区
                                                          .toInstant()
                                                          .toEpochMilli())
                                         .locationDisplayName(entity.getLocationDisplayName())
                                         .isCustomLocation(entity.isCustomLocation())
                                         .customTagName(entity.getCustomTagName().orElse(null))
                                         .rawCoordinates(RawCoordinate.builder()
                                                                      .latitude(entity.getRawLatitude().orElse(null))
                                                                      .longitude(entity.getRawLongitude().orElse(null))
                                                                      .build())
                                         .notes(entity.getNotes().orElse(null))
                                         .activityTypeId(entity.getActivityTypeId().orElse(null))
                                         .clothingLevel(entity.getClothingLevel().orElse(null))
                                         .adjustedTempLevel(entity.getAdjustedTempLevel().orElse(null))
                                         .adjustedHumidLevel(entity.getAdjustedHumidLevel().orElse(null))
                                         .build();
    }

    public static ComfortFeedbackEntity toPartialEntityForUpdate(ComfortFeedbackResponseDTO dto, String userId) {
        return ComfortFeedbackEntity.builder()
                                    .feedbackId(dto.feedbackId())
                                    .userId(userId)
                                    .timestamp(LocalDateTime.ofInstant(
                                            Instant.ofEpochMilli(dto.timestamp()), ZoneId.systemDefault()))
                                    .comfortLevel(dto.comfortLevel())
                                    .feedbackType(dto.feedbackType())
                                    .activityTypeId(Optional.ofNullable(dto.activityTypeId()))
                                    .clothingLevel(Optional.ofNullable(dto.clothingLevel()))
                                    .adjustedTempLevel(Optional.ofNullable(dto.adjustedTempLevel()))
                                    .adjustedHumidLevel(Optional.ofNullable(dto.adjustedHumidLevel()))
                                    .notes(Optional.ofNullable(dto.notes()))
                                    .customTagName(Optional.ofNullable(dto.customTagName()))
                                    .locationDisplayName(dto.locationDisplayName())
                                    .isCustomLocation(Boolean.TRUE.equals(dto.isCustomLocation()))
                                    .rawLatitude(Optional.ofNullable(dto.rawCoordinates().latitude()))
                                    .rawLongitude(Optional.ofNullable(dto.rawCoordinates().longitude()))
                                    .build(); // 👈 其他字段由 Service 层查旧值补上
    }

    public static ComfortFeedbackWithReadingDTO toResponseDTO(ComfortFeedbackWithReadingVO vo) {
        return ComfortFeedbackWithReadingDTO.builder()
                                            .feedbackId(vo.getFeedbackId())
                                            .comfortLevel(vo.getComfortLevel())
                                            .feedbackType(vo.getFeedbackType())
                                            .timestamp(vo.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                                            .locationDisplayName(vo.getLocationDisplayName())
                                            .isCustomLocation(vo.isCustomLocation())
                                            .customTagName(vo.getCustomTagName().orElse(null))
                                            .rawCoordinates(RawCoordinate.builder()
                                                                         .latitude(vo.getRawLatitude().orElse(null))
                                                                         .longitude(vo.getRawLongitude().orElse(null))
                                                                         .build())
                                            .notes(vo.getNotes().orElse(null))
                                            .activityTypeId(vo.getActivityTypeId().orElse(null))
                                            .clothingLevel(vo.getClothingLevel().orElse(null))
                                            .adjustedTempLevel(vo.getAdjustedTempLevel().orElse(null))
                                            .adjustedHumidLevel(vo.getAdjustedHumidLevel().orElse(null))
                                            .temperature(vo.getTemperature().orElse(null))
                                            .humidity(vo.getHumidity().orElse(null))
                                            .build();
    }

    public static RawCoordinate convertCoordinates(Object raw) {
        if (raw == null) return null;

        if (raw instanceof PGobject pgObj) {
            String value = pgObj.getValue(); // "POINT(-9.05 53.27)"
            return parsePoint(value);
        } else if (raw instanceof String str) {
            return parsePoint(str);
        } else if (raw instanceof Map<?, ?> map) {
            Object x = map.get("x");
            Object y = map.get("y");
            if (x instanceof Number && y instanceof Number) {
                return new RawCoordinate(((Number) y).doubleValue(), ((Number) x).doubleValue());
            }
        }

        throw new IllegalArgumentException("Unsupported rawCoordinates format: " + raw.getClass());
    }

    private static RawCoordinate parsePoint(String value) {
        if (!value.startsWith("POINT(")) throw new IllegalArgumentException("Invalid point: " + value);
        String[] parts = value.substring(6, value.length() - 1).split(" ");
        double lon = Double.parseDouble(parts[0]);
        double lat = Double.parseDouble(parts[1]);
        return new RawCoordinate(lat, lon);
    }
}
