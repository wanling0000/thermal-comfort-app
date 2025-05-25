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
}
