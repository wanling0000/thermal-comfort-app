package com.wanling.infrastructure.persistent.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.entity.DailyComfortStatEntity;
import com.wanling.domain.environmental.model.valobj.FeedbackSnapshot;
import com.wanling.domain.environmental.repository.IComfortFeedbackRepository;
import com.wanling.domain.environmental.repository.ILocationTagRepository;
import com.wanling.domain.environmental.repository.IUserLocationRepository;
import com.wanling.infrastructure.persistent.mapper.ComfortFeedbackMapper;
import com.wanling.infrastructure.persistent.po.ComfortFeedback;
import com.wanling.infrastructure.persistent.po.DailyComfortStatRow;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ComfortFeedbackRepository implements IComfortFeedbackRepository {
    private final ComfortFeedbackMapper comfortFeedbackMapper;
    private final ILocationTagRepository locationTagRepository;
    private final IUserLocationRepository userLocationRepository;

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

    @Override
    public List<FeedbackSnapshot> findSnapshotsByUserIdAndTimeRange(String userId, LocalDateTime from, LocalDateTime to) {
        List<ComfortFeedback> poList = comfortFeedbackMapper.findByUserIdAndTimeRange(userId, from, to);

        return poList.stream()
                     .map(po -> new FeedbackSnapshot(
                             po.getFeedbackId(),
                             Optional.ofNullable(po.getComfortLevel()).orElse(10),
                             Optional.ofNullable(po.getNotes()),
                             Optional.ofNullable(po.getActivityTypeId()),
                             Optional.ofNullable(po.getClothingLevel()),
                             po.getReadingId()
                     ))
                     .collect(Collectors.toList());
    }

    @Override
    public List<DailyComfortStatEntity> findDailyStatsForUserInYear(String userId, LocalDate startDate, LocalDate endDate) {
        List<DailyComfortStatRow> rawList = comfortFeedbackMapper.findDailyStatsForUserInYear(userId, startDate, endDate);

        return rawList.stream()
                      .map(raw -> new DailyComfortStatEntity(
                              raw.date(),
                              Optional.ofNullable(raw.averageComfort()),
                              raw.feedbackCount()
                      ))
                      .collect(Collectors.toList());
    }

    @Override
    public List<ComfortFeedbackEntity> findByUserAndDate(String userId, LocalDate date) {
        List<ComfortFeedback> poList = comfortFeedbackMapper.selectByUserIdAndDate(userId, date);

        List<ComfortFeedbackEntity> entities = poList.stream()
                                                     .map(this::toEntity)
                                                     .toList();
        return entities;
    }

    @Override
    public List<ComfortFeedbackEntity> findByUserAndDateRange(String userId, LocalDate start, LocalDate end) {
        List<ComfortFeedback> poList = comfortFeedbackMapper.selectByUserIdAndDateRange(userId, start, end);
        return poList.stream()
                     .map(this::toEntity)
                     .toList();
    }

    private ComfortFeedbackEntity toEntity(ComfortFeedback po) {
        Optional<String> userLocationTagId = Optional.ofNullable(po.getUserLocationTagId());

        final Optional<String>[] customTagName = new Optional[]{Optional.empty()};
        final String[] displayName = new String[]{"(unknown location)"};

        // 如果有自定义标签，则标记为 true，并尝试获取 customName
        boolean isCustom = userLocationTagId.isPresent();

        userLocationTagId.ifPresent(tagId -> {
            userLocationRepository.findById(tagId).ifPresent(userTag -> {
                if (userTag.getName() != null) {
                    customTagName[0] = Optional.of(userTag.getName());
                }
            });
        });

        if (po.getLocationTagId() != null) {
            locationTagRepository.findById(po.getLocationTagId()).ifPresent(tag -> {
                if (tag.getDisplayName() != null) {
                    displayName[0] = tag.getDisplayName();
                }
            });
        }

        return ComfortFeedbackEntity.builder()
                                    .feedbackId(po.getFeedbackId())
                                    .userId(po.getUserId())
                                    .timestamp(po.getTimestamp())
                                    .comfortLevel(po.getComfortLevel())
                                    .feedbackType(po.getFeedbackType())
                                    .activityTypeId(Optional.ofNullable(po.getActivityTypeId()))
                                    .clothingLevel(Optional.ofNullable(po.getClothingLevel()))
                                    .adjustedTempLevel(Optional.ofNullable(po.getAdjustedTempLevel()))
                                    .adjustedHumidLevel(Optional.ofNullable(po.getAdjustedHumidLevel()))
                                    .notes(Optional.ofNullable(po.getNotes()))
                                    .locationTagId(po.getLocationTagId())
                                    .userLocationTagId(userLocationTagId)
                                    .readingId(po.getReadingId())
                                    .rawLatitude(extractLatitude(po.getRawCoordinates()))
                                    .rawLongitude(extractLongitude(po.getRawCoordinates()))
                                    .locationDisplayName(displayName[0])
                                    .isCustomLocation(isCustom)
                                    .customTagName(customTagName[0])
                                    .build();
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
