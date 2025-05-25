package com.wanling.domain.environmental.service.impl;

import java.util.Optional;
import java.util.UUID;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.repository.ILocationTagRepository;
import com.wanling.domain.environmental.service.ILocationTagService;
import com.wanling.domain.environmental.service.IUserLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationTagServiceImpl implements ILocationTagService {
    private final ILocationTagRepository locationTagRepository;
    @Override
    public String findOrCreate(LocationCandidateVO candidate) {
        Optional<LocationTagEntity> existing = this.findExisting(candidate);
        if (existing.isPresent()) {
            return existing.get().getLocationTagId();
        }

        // Create new tag
        LocationTagEntity newTag = LocationTagEntity.builder()
                                                    .locationTagId(UUID.randomUUID().toString())
                                                    .displayName(candidate.getDisplayName())
                                                    .latitude(candidate.getLatitude())
                                                    .longitude(candidate.getLongitude())
                                                    .isCustom(false)
                                                    .build();

        locationTagRepository.insertLocationTag(newTag);
        return newTag.getLocationTagId();
    }

    @Override
    public Optional<LocationTagEntity> findExisting(LocationCandidateVO candidate) {
        return locationTagRepository.findNearbyWithSameName(
                candidate.getDisplayName(),
                candidate.getLatitude(),
                candidate.getLongitude(),
                50 // meters, configurable
        );
    }
}
