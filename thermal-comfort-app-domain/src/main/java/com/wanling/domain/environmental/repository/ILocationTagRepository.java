package com.wanling.domain.environmental.repository;

import java.util.Optional;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 20/05/2025
 * 18:05
 */
public interface ILocationTagRepository {
    Optional<LocationTagEntity> findNearbyWithSameName(String name, double lat, double lng, double radiusMeters);
    void insertLocationTag(LocationTagEntity entity);
    Optional<LocationTagEntity> findById(String locationTagId);
    void updateIsCustom(String id, boolean isCustom);

}
