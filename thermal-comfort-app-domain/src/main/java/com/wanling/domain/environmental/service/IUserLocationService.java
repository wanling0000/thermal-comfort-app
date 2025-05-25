package com.wanling.domain.environmental.service;

import java.util.Optional;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.entity.UserLocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;

public interface IUserLocationService {

    /**
     * Create a user-defined location tag
     * 1. if the user selects “Bind current location” and passes latitude and longitude:
     * - Try to find a match in location_tags (call LocationTagService.findOrCreate(...))
     * - Set user_location_tags.related_location_tag_id to the ID of the system tag found/created above.
     * 2. And finally return this user_location_tag_id
     */
    String createCustomTag(String userId, String name, Optional<LocationCandidateVO> location);

    /**
     * Resolve the system tag associated with a user tag
     */
    Optional<LocationTagEntity> resolveToSystemTag(String userLocationTagId);

    Optional<UserLocationTagEntity> findByUserAndName(String userId, String tagName);
}