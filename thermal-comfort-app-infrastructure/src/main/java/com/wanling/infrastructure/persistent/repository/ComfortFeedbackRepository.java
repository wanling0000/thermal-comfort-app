package com.wanling.infrastructure.persistent.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.repository.IComfortFeedbackRepository;
import com.wanling.infrastructure.persistent.mapper.ComfortFeedbackMapper;
import com.wanling.infrastructure.persistent.po.ComfortFeedback;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBReader;
import org.springframework.stereotype.Repository;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 22/05/2025
 * 21:56
 */

@Slf4j
@Repository
public class ComfortFeedbackRepository implements IComfortFeedbackRepository {
    @Resource
    private ComfortFeedbackMapper comfortFeedbackMapper;

    @Override
    public void saveFeedback(ComfortFeedbackEntity entity) {
        ComfortFeedback po = new ComfortFeedback();

        po.setFeedbackId(entity.getFeedbackId());
        po.setUserId(entity.getUserId());
        po.setTimestamp(entity.getTimestamp());
        po.setComfortLevel(entity.getComfortLevel());
        po.setFeedbackType(entity.getFeedbackType());

        po.setActivityTypeId(entity.getActivityTypeId().orElse(null));
        po.setClothingLevel(entity.getClothingLevel().orElse(null));
        po.setAdjustedTempLevel(entity.getAdjustedTempLevel().orElse(null));
        po.setAdjustedHumidLevel(entity.getAdjustedHumidLevel().orElse(null));
        po.setNotes(entity.getNotes().orElse(null));

        po.setLocationTagId(entity.getLocationTagId());
        po.setUserLocationTagId(entity.getUserLocationTagId().orElse(null));
        po.setReadingId(entity.getReadingId());

        po.setRawCoordinates(String.format("POINT(%f %f)", entity.getRawLongitude(), entity.getRawLatitude()));

        comfortFeedbackMapper.insert(po);
    }

    @Override
    public List<ComfortFeedbackEntity> findAllByUserIdOrderByTimestampDesc(String userId) {
        List<ComfortFeedback> poList = comfortFeedbackMapper.findAllByUserIdOrderByTimestampDesc(userId);

        return poList.stream()
                     .map(po -> {
                         return ComfortFeedbackEntity.builder()
                                                     .feedbackId(po.getFeedbackId())
                                                     .userId(po.getUserId())
                                                     .timestamp(po.getTimestamp())
                                                     .comfortLevel(Optional.ofNullable(po.getComfortLevel()).orElse(10)) // 异常处理
                                                     .feedbackType(po.getFeedbackType())
                                                     .activityTypeId(Optional.ofNullable(po.getActivityTypeId()))
                                                     .clothingLevel(Optional.ofNullable(po.getClothingLevel()))
                                                     .adjustedTempLevel(Optional.ofNullable(po.getAdjustedTempLevel()))
                                                     .adjustedHumidLevel(Optional.ofNullable(po.getAdjustedHumidLevel()))
                                                     .notes(Optional.ofNullable(po.getNotes()))
                                                     .locationTagId(po.getLocationTagId())
                                                     .userLocationTagId(Optional.ofNullable(po.getUserLocationTagId()))
                                                     .readingId(po.getReadingId())
                                                     .rawLatitude(extractLatitude(po.getRawCoordinates()))
                                                     .rawLongitude(extractLongitude(po.getRawCoordinates()))
                                                     // placeholder 字段
                                                     .locationDisplayName(String.valueOf(Optional.ofNullable(po.getLocationTagId()))) // 延后填充
                                                     .isCustomLocation(po.getUserLocationTagId() != null)
                                                     .customTagName(Optional.empty())
                                                     .build();
                     })
                     .collect(Collectors.toList());
    }

    @Override
    public Optional<ComfortFeedbackEntity> findLatestByUserId(String userId) {
        ComfortFeedback po = comfortFeedbackMapper.findLatestByUserId(userId);
        if (po == null) return Optional.empty();
        ComfortFeedbackEntity entity = ComfortFeedbackEntity.builder()
                                                             .feedbackId(po.getFeedbackId())
                                                             .userId(po.getUserId())
                                                             .timestamp(po.getTimestamp())
                                                             .comfortLevel(Optional.ofNullable(po.getComfortLevel()).orElse(10)) // 异常处理
                                                             .feedbackType(po.getFeedbackType())
                                                             .activityTypeId(Optional.ofNullable(po.getActivityTypeId()))
                                                             .clothingLevel(Optional.ofNullable(po.getClothingLevel()))
                                                             .adjustedTempLevel(Optional.ofNullable(po.getAdjustedTempLevel()))
                                                             .adjustedHumidLevel(Optional.ofNullable(po.getAdjustedHumidLevel()))
                                                             .notes(Optional.ofNullable(po.getNotes()))
                                                             .locationTagId(po.getLocationTagId())
                                                             .userLocationTagId(Optional.ofNullable(po.getUserLocationTagId()))
                                                             .readingId(po.getReadingId())
                                                             .rawLatitude(extractLatitude(po.getRawCoordinates()))
                                                             .rawLongitude(extractLongitude(po.getRawCoordinates()))
                                                             // placeholder 字段
                                                             .locationDisplayName(String.valueOf(Optional.ofNullable(po.getLocationTagId()))) // 延后填充
                                                             .isCustomLocation(po.getUserLocationTagId() != null)
                                                             .customTagName(Optional.empty())
                                                             .build();

        return Optional.of(entity);
    }

    private Optional<Double> extractLatitude(String wkbHex) {
        if (wkbHex == null) return Optional.empty();
        try {
            byte[] wkb = WKBReader.hexToBytes(wkbHex); // 注意这里
            WKBReader reader = new WKBReader();
            Point point = (Point) reader.read(wkb);
            return Optional.of(point.getY());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    private Optional<Double> extractLongitude(String wkbHex) {
        if (wkbHex == null) return Optional.empty();
        try {
            byte[] wkb = WKBReader.hexToBytes(wkbHex); // 注意这里
            WKBReader reader = new WKBReader();
            Point point = (Point) reader.read(wkb);
            return Optional.of(point.getX());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
