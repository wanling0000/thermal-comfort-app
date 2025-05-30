package com.wanling.domain.environmental.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.model.entity.UserLocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.repository.IComfortFeedbackRepository;
import com.wanling.domain.environmental.repository.IEnvironmentalReadingRepository;
import com.wanling.domain.environmental.service.IComfortFeedbackService;
import com.wanling.domain.environmental.service.ILocationTagService;
import com.wanling.domain.environmental.service.IUserLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 22/05/2025
 * 12:30
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ComfortFeedbackService implements IComfortFeedbackService {
    private final IComfortFeedbackRepository feedbackRepository;
    private final ILocationTagService locationTagService;
    private final IUserLocationService userLocationService;
    private final IEnvironmentalReadingRepository readingRepository;
    @Override
    public void handleFeedbackWithReading(ComfortFeedbackEntity feedback, EnvironmentalReadingEntity reading) {
        String feedbackId = feedback.getFeedbackId() != null
                ? feedback.getFeedbackId()
                : UUID.randomUUID().toString();

        String userId = "admin"; // TODO: replace with actual authenticated user

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
                locationTagId = locationTagService.findOrCreate(loc);
            }
        } else {
            locationTagId = locationTagService.findOrCreate(loc);
        }

        // 3: Save the reading with associated location
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
}
