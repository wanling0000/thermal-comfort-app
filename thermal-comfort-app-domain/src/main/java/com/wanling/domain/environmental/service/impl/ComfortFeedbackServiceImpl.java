package com.wanling.domain.environmental.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.model.entity.UserLocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.repository.IComfortFeedbackRepository;
import com.wanling.domain.environmental.repository.IEnvironmentalReadingRepository;
import com.wanling.domain.environmental.repository.ILocationTagRepository;
import com.wanling.domain.environmental.repository.IUserLocationRepository;
import com.wanling.domain.environmental.service.IComfortFeedbackService;
import com.wanling.domain.environmental.service.ILocationTagService;
import com.wanling.domain.environmental.service.IUserLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComfortFeedbackServiceImpl implements IComfortFeedbackService {
    private final IComfortFeedbackRepository feedbackRepository;
    private final ILocationTagService locationTagService;
    private final IUserLocationService userLocationService;
    private final IEnvironmentalReadingRepository readingRepository;
    private final ILocationTagRepository locationTagRepository;
    private final IUserLocationRepository userLocationRepository;
    @Override
    public void handleFeedbackWithReading(ComfortFeedbackEntity feedback, EnvironmentalReadingEntity reading) {
        String feedbackId = feedback.getFeedbackId() != null
                ? feedback.getFeedbackId()
                : UUID.randomUUID().toString();
        String userId = feedback.getUserId();

        // 1: Prepare location candidate from reading, NOT from feedback
        LocationCandidateVO loc = reading.getLocation();

        String userLocationTagId = null;
        String locationTagId;

        // 2: If location is custom, try to reuse existing user tag
        if (loc.isCustom() != null && loc.isCustom() && loc.getCustomTag() != null) {
            String tagName = loc.getCustomTag();
            Optional<UserLocationTagEntity> existing = userLocationService.findByUserAndName(userId, tagName);

            if (existing.isPresent()) {
                userLocationTagId = existing.get().getUserLocationTagId();
                locationTagId = existing.get().getRelatedLocationTagId();
            } else {
                userLocationTagId = userLocationService.createCustomTag(userId, tagName, Optional.of(loc));
                locationTagId = locationTagService.normalizeAndFindOrCreate(loc, userId);
            }
        } else {
            locationTagId = locationTagService.normalizeAndFindOrCreate(loc, userId);
        }

        // 3: Save the reading with associated location
        reading.setUserId(userId);
        reading.setLocationTagId(locationTagId);
        readingRepository.saveEnvironmentalReadings(List.of(reading));

        // 4: Finalize feedback entity and save
        ComfortFeedbackEntity completed = feedback.toBuilder()
                                                  .feedbackId(feedbackId)
                                                  .userId(userId)
                                                  .locationTagId(locationTagId)
                                                  .userLocationTagId(Optional.ofNullable(userLocationTagId))
                                                  .readingId(reading.getReadingId())
                                                  .build();

        feedbackRepository.saveFeedback(completed);

        log.info("Feedback and reading saved. FeedbackId={}, ReadingId={}", feedbackId, reading.getReadingId());
    }

    public List<ComfortFeedbackEntity> getFeedbackByMonth(int year, int month, String userId) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        List<ComfortFeedbackEntity> comfortFeedbackEntityList =
                new ArrayList<>(feedbackRepository.findByUserAndDateRange(userId, start, end));

        Collections.reverse(comfortFeedbackEntityList);

        return comfortFeedbackEntityList.stream()
                                        .map(e -> {
                                            final String[] displayName = {"(unknown location)"};
                                            final Optional<String>[] customName = new Optional[]{Optional.empty()};

                                            // 查系统标签 displayName
                                            locationTagRepository.findById(e.getLocationTagId()).ifPresent(tag -> {
                                                if (tag.getDisplayName() != null) {
                                                    displayName[0] = tag.getDisplayName();
                                                }
                                            });

                                            // 查用户自定义标签（如果有）——只设置 customTagName，不覆盖 displayName
                                            e.getUserLocationTagId().ifPresent(userLocationTagId -> {
                                                userLocationRepository.findById(userLocationTagId).ifPresent(userTag -> {
                                                    if (userTag.getName() != null) {
                                                        customName[0] = Optional.of(userTag.getName());
                                                    }
                                                });
                                            });

                                            return ComfortFeedbackEntity.builder()
                                                                        .feedbackId(e.getFeedbackId())
                                                                        .userId(e.getUserId())
                                                                        .timestamp(e.getTimestamp())
                                                                        .comfortLevel(e.getComfortLevel())
                                                                        .feedbackType(e.getFeedbackType())
                                                                        .activityTypeId(e.getActivityTypeId())
                                                                        .clothingLevel(e.getClothingLevel())
                                                                        .adjustedTempLevel(e.getAdjustedTempLevel())
                                                                        .adjustedHumidLevel(e.getAdjustedHumidLevel())
                                                                        .notes(e.getNotes())
                                                                        .locationTagId(e.getLocationTagId())
                                                                        .userLocationTagId(e.getUserLocationTagId())
                                                                        .readingId(e.getReadingId())
                                                                        .rawLatitude(e.getRawLatitude())
                                                                        .rawLongitude(e.getRawLongitude())
                                                                        .locationDisplayName(displayName[0])
                                                                        .isCustomLocation(e.isCustomLocation())
                                                                        .customTagName(customName[0])
                                                                        .build();
                                        })
                                        .toList();
    }

    @Override
    public ComfortFeedbackEntity findLatestFeedbackForCurrentUser(String userId) {
        Optional<ComfortFeedbackEntity> optional = feedbackRepository.findLatestByUserId(userId);

        if (optional.isEmpty()) {
            throw new RuntimeException("Has no feedback record");
        }
        ComfortFeedbackEntity e = optional.get();

        final String[] displayName = {"(unknown location)"};
        final Optional<String>[] customName = new Optional[]{Optional.empty()};

        // 查系统标签 displayName
        locationTagRepository.findById(e.getLocationTagId()).ifPresent(tag -> {
            if (tag.getDisplayName() != null) {
                displayName[0] = tag.getDisplayName();
            }
        });

        // 查用户自定义标签（如果有）——只设置 customTagName，不覆盖 displayName
        e.getUserLocationTagId().ifPresent(userLocationTagId -> {
            userLocationRepository.findById(userLocationTagId).ifPresent(userTag -> {
                if (userTag.getName() != null) {
                    customName[0] = Optional.of(userTag.getName());
                }
            });
        });

        return e.toBuilder()
                .locationDisplayName(displayName[0])
                .customTagName(customName[0])
                .build();
    }

    @Override
    public void deleteFeedback(String id, String userId) {
        Optional<ComfortFeedbackEntity> optional = feedbackRepository.findById(id);

        if (optional.isEmpty()) {
            log.warn("❌ Feedback not found, id = {}", id);
            throw new RuntimeException("Feedback not found");
        }

        ComfortFeedbackEntity entity = optional.get();

        // 安全性检查：只能删除自己的反馈
        if (!entity.getUserId().equals(userId)) {
            log.warn("⛔ Unauthorized delete attempt. userId = {}, feedback.userId = {}", userId, entity.getUserId());
            throw new RuntimeException("Unauthorized to delete this feedback");
        }

        // 删除反馈记录（软删除）
        feedbackRepository.markAsDeleted(id, userId);
        log.info("🗑️ Feedback soft-deleted: id = {}, userId = {}", id, userId);
    }

    @Override
    public void updateFeedback(ComfortFeedbackEntity partialEntity) {
        String feedbackId = partialEntity.getFeedbackId();
        String userId = partialEntity.getUserId();

        ComfortFeedbackEntity existing = feedbackRepository.findById(feedbackId)
                                                           .filter(e -> e.getUserId().equals(userId))
                                                           .orElseThrow(() -> new RuntimeException("Feedback not found or unauthorized"));

        // Step 1️⃣ 解析用户上传的 raw 经纬度 → 构建 LocationCandidateVO
        Optional<Double> latOpt = partialEntity.getRawLatitude();
        Optional<Double> lonOpt = partialEntity.getRawLongitude();

        if (latOpt.isEmpty() || lonOpt.isEmpty()) {
            throw new IllegalArgumentException("Missing coordinates");
        }

        LocationCandidateVO location = LocationCandidateVO.builder()
                                                          .latitude(latOpt.get())
                                                          .longitude(lonOpt.get())
                                                          .displayName(partialEntity.getLocationDisplayName())
                                                          .customTag(partialEntity.getCustomTagName().orElse(null))
                                                          .isCustom(partialEntity.isCustomLocation())
                                                          .build();

        // Step 2️⃣ 绑定 locationTagId / userLocationTagId（与创建逻辑相同）
        String locationTagId;
        String userLocationTagId = null;

        if (location.isCustom() != null && location.isCustom() && location.getCustomTag() != null) {
            Optional<UserLocationTagEntity> existingTag = userLocationService.findByUserAndName(userId, location.getCustomTag());

            if (existingTag.isPresent()) {
                userLocationTagId = existingTag.get().getUserLocationTagId();
                locationTagId = existingTag.get().getRelatedLocationTagId();
            } else {
                userLocationTagId = userLocationService.createCustomTag(userId, location.getCustomTag(), Optional.of(location));
                locationTagId = locationTagService.normalizeAndFindOrCreate(location, userId);
            }
        } else {
            locationTagId = locationTagService.normalizeAndFindOrCreate(location, userId);
        }

        // Step 3️⃣ 构建可更新字段（类似 partial update）
        ComfortFeedbackEntity updated = existing.toBuilder()
                                                .timestamp(partialEntity.getTimestamp())
                                                .comfortLevel(partialEntity.getComfortLevel())
                                                .feedbackType(partialEntity.getFeedbackType())
                                                .activityTypeId(partialEntity.getActivityTypeId())
                                                .clothingLevel(partialEntity.getClothingLevel())
                                                .adjustedTempLevel(partialEntity.getAdjustedTempLevel())
                                                .adjustedHumidLevel(partialEntity.getAdjustedHumidLevel())
                                                .notes(partialEntity.getNotes())
                                                .rawLatitude(partialEntity.getRawLatitude())
                                                .rawLongitude(partialEntity.getRawLongitude())
                                                .locationTagId(locationTagId)
                                                .userLocationTagId(Optional.ofNullable(userLocationTagId))
                                                .build();

        feedbackRepository.updateEditableFields(updated);

        log.info("✅ Feedback updated: feedbackId={}, locationTagId={}, userTag={}",
                feedbackId, locationTagId, userLocationTagId);
    }
}
