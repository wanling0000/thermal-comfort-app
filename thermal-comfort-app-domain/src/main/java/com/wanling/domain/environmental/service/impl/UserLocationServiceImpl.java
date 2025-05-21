package com.wanling.domain.environmental.service.impl;

import java.util.Optional;
import java.util.UUID;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.entity.UserLocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.repository.IUserLocationRepository;
import com.wanling.domain.environmental.service.ILocationTagService;
import com.wanling.domain.environmental.service.IUserLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 20/05/2025
 * 18:08
 */

@Service
@RequiredArgsConstructor
public class UserLocationServiceImpl implements IUserLocationService {
    private final IUserLocationRepository userLocationRepository;
    private final ILocationTagService locationTagService;
    @Override
    public String createCustomTag(String userId, String name, Optional<LocationCandidateVO> locationOpt) {
        String tagId = UUID.randomUUID().toString();
        String relatedSystemTagId = null;

        if (locationOpt.isPresent()) {
            LocationCandidateVO location = locationOpt.get();
            relatedSystemTagId = locationTagService.findOrCreate(location);
        }

        UserLocationTagEntity tagEntity = UserLocationTagEntity.builder()
                                                               .userLocationTagId(tagId)
                                                               .userId(userId)
                                                               .name(name)
                                                               .latitude(locationOpt.map(LocationCandidateVO::getLatitude).orElse(null))
                                                               .longitude(locationOpt.map(LocationCandidateVO::getLongitude).orElse(null))
                                                               .relatedLocationTagId(relatedSystemTagId)
                                                               .note(null)
                                                               .build();

        userLocationRepository.insertUserLocationTag(tagEntity);

        return tagId;
    }

    @Override
    public Optional<LocationTagEntity> resolveToSystemTag(String userLocationTagId) {
        return userLocationRepository.resolveToSystemTag(userLocationTagId);
    }
}
