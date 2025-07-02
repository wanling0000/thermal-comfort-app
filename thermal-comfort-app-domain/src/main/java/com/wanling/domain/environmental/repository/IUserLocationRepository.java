package com.wanling.domain.environmental.repository;

import java.util.List;
import java.util.Optional;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.entity.UserLocationTagEntity;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 21/05/2025
 * 12:32
 */
public interface IUserLocationRepository {
    void insertUserLocationTag(UserLocationTagEntity tagEntity);

    Optional<LocationTagEntity> resolveToSystemTag(String userLocationTagId);

    Optional<UserLocationTagEntity> findByUserAndName(String userId, String tagName);

    Optional<UserLocationTagEntity> findById(String userLocationTagId);

    List<UserLocationTagEntity> findByUserId(String userId);
}
