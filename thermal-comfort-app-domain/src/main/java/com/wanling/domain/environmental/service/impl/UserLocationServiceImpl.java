package com.wanling.domain.environmental.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.entity.UserLocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.model.valobj.LocationPreviewVO;
import com.wanling.domain.environmental.repository.ILocationTagRepository;
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
    private final ILocationTagRepository locationTagRepository;
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

    @Override
    public Optional<UserLocationTagEntity> findByUserAndName(String userId, String tagName) {
        return userLocationRepository.findByUserAndName(userId, tagName);
    }

    @Override
    public List<LocationPreviewVO> findAllByUserId(String userId) {
        List<UserLocationTagEntity> userTags = userLocationRepository.findByUserId(userId);

        return userTags.stream()
                       .map(tag -> {
                           String displayName = tag.getName();
                           Double latitude = tag.getLatitude();
                           Double longitude = tag.getLongitude();

                           // 如果有关联的系统标签，补充 displayName 和坐标（若缺失）
                           if (tag.getRelatedLocationTagId() != null) {
                               Optional<LocationTagEntity> systemTagOpt = locationTagRepository.findById(tag.getRelatedLocationTagId());
                               if (systemTagOpt.isPresent()) {
                                   LocationTagEntity systemTag = systemTagOpt.get();
                                   displayName = systemTag.getDisplayName();
                                   if (latitude == null) latitude = systemTag.getLatitude();
                                   if (longitude == null) longitude = systemTag.getLongitude();
                               }
                           }

                           return new LocationPreviewVO(
                                   displayName,
                                   true,
                                   tag.getName(),
                                   latitude,
                                   longitude
                           );
                       })
                       .toList();
    }

}
