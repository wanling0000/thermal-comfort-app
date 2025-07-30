package com.wanling.domain.environmental.service.impl;

import java.util.Optional;
import java.util.UUID;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.entity.UserLocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.repository.ILocationTagRepository;
import com.wanling.domain.environmental.repository.IUserLocationRepository;
import com.wanling.domain.environmental.service.ILocationTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationTagServiceImpl implements ILocationTagService {
    private final ILocationTagRepository locationTagRepository;
    private final IUserLocationRepository userLocationRepository;

    @Override
    public String normalizeAndFindOrCreate(LocationCandidateVO candidate, String userId) {
        // 1. 如果是自定义标签，并包含 tag 名，优先从 user_location_tags 里找
        if (Boolean.TRUE.equals(candidate.getIsCustom()) && candidate.getCustomTag() != null) {
            Optional<UserLocationTagEntity> userTag =
                    userLocationRepository.findByUserAndName(userId, candidate.getCustomTag());
            if (userTag.isPresent()) {
                return userTag.get().getRelatedLocationTagId(); // 已经建好了
            }
        }

        // 2. 否则，尝试自动坐标匹配（在系统标签中找现成的）
        Optional<LocationTagEntity> existing = findExisting(candidate);
        if (existing.isPresent()) {
            LocationTagEntity tag = existing.get();

            // 如果当前用户视角是 isCustom=true，但旧 tag 不是，就更新一下
            if (Boolean.TRUE.equals(candidate.getIsCustom()) && !Boolean.TRUE.equals(tag.getIsCustom())) {
                tag.setIsCustom(true);
                locationTagRepository.updateIsCustom(tag.getLocationTagId(), true);
            }

            return existing.get().getLocationTagId();
        }

        // 3. 新建
        boolean isCustom = Boolean.TRUE.equals(candidate.getIsCustom());

        LocationTagEntity newTag = LocationTagEntity.builder()
                                                    .locationTagId(UUID.randomUUID().toString())
                                                    .displayName(candidate.getDisplayName())
                                                    .latitude(candidate.getLatitude())
                                                    .longitude(candidate.getLongitude())
                                                    .isCustom(isCustom)
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
