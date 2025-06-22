package com.wanling.infrastructure.persistent.repository;

import java.util.Optional;

import javax.annotation.Resource;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.repository.ILocationTagRepository;
import com.wanling.infrastructure.persistent.mapper.LocationTagsMapper;
import com.wanling.infrastructure.persistent.po.LocationTags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 20/05/2025
 * 18:06
 */

@Slf4j
@Repository
public class LocationTagRepository implements ILocationTagRepository {
    @Resource
    private LocationTagsMapper locationTagsMapper;

    @Override
    public Optional<LocationTagEntity> findNearbyWithSameName(String name, double lat, double lng, double radiusMeters) {
        LocationTags po = locationTagsMapper.findNearbyWithSameName(name, lat, lng);
        if (po == null) {
            return Optional.empty();
        }
        LocationTagEntity entity = new LocationTagEntity();
        BeanUtils.copyProperties(po, entity);
        return Optional.of(entity);
    }

    @Override
    public void insertLocationTag(LocationTagEntity entity) {
        LocationTags po = new LocationTags();
        BeanUtils.copyProperties(entity, po);
        locationTagsMapper.insertLocationTag(po);
        log.info("✅ Inserted new location tag: {}", entity.getDisplayName());
    }

    @Override
    public Optional<LocationTagEntity> findById(String locationTagId) {
        LocationTags po = locationTagsMapper.findById(locationTagId);
        if (po == null) return Optional.empty();
        return Optional.of(LocationTagEntity.builder()
                                            .locationTagId(po.getLocationTagId())
                                            .displayName(po.getDisplayName())
                                            .latitude(po.getLatitude())
                                            .longitude(po.getLongitude())
                                            .isCustom(po.getIsCustom())
                                            .createdAt(String.valueOf(po.getCreatedAt()))
                                            .build());
    }
}
