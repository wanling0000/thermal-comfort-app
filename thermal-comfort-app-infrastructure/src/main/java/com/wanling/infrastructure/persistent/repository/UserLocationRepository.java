package com.wanling.infrastructure.persistent.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.entity.UserLocationTagEntity;
import com.wanling.domain.environmental.repository.IUserLocationRepository;
import com.wanling.infrastructure.persistent.mapper.LocationTagsMapper;
import com.wanling.infrastructure.persistent.mapper.UserLocationTagsMapper;
import com.wanling.infrastructure.persistent.po.LocationTags;
import com.wanling.infrastructure.persistent.po.UserLocationTags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class UserLocationRepository implements IUserLocationRepository {
    @Resource
    private UserLocationTagsMapper userLocationTagsMapper;

    @Resource
    private LocationTagsMapper locationTagsMapper;

    @Override
    public void insertUserLocationTag(UserLocationTagEntity tagEntity) {
        UserLocationTags po = new UserLocationTags();
        po.setUserLocationTagId(tagEntity.getUserLocationTagId());
        po.setUserId(tagEntity.getUserId());
        po.setName(tagEntity.getName());
        po.setLatitude(tagEntity.getLatitude());
        po.setLongitude(tagEntity.getLongitude());
        po.setNote(tagEntity.getNote());
        po.setRelatedLocationTagId(tagEntity.getRelatedLocationTagId());

        po.setCreatedAt(LocalDateTime.now());

        userLocationTagsMapper.insert(po);
    }

    @Override
    public Optional<LocationTagEntity> resolveToSystemTag(String userLocationTagId) {
        UserLocationTags userTag = userLocationTagsMapper.selectByPrimaryKey(userLocationTagId);
        if (userTag == null || userTag.getRelatedLocationTagId() == null) {
            return Optional.empty();
        }

        LocationTags locationTag = locationTagsMapper.findById(userTag.getRelatedLocationTagId());
        if (locationTag == null) {
            return Optional.empty();
        }

        LocationTagEntity entity = LocationTagEntity.builder()
                                                    .locationTagId(locationTag.getLocationTagId())
                                                    .displayName(locationTag.getDisplayName())
                                                    .latitude(locationTag.getLatitude())
                                                    .longitude(locationTag.getLongitude())
                                                    .isCustom(locationTag.getIsCustom())
                                                    .createdAt(locationTag.getCreatedAt() != null
                                                            ? locationTag.getCreatedAt().toString()
                                                            : null)
                                                    .build();

        return Optional.of(entity);
    }

    @Override
    public Optional<UserLocationTagEntity> findByUserAndName(String userId, String tagName) {
        UserLocationTags po = userLocationTagsMapper.selectByUserAndName(userId, tagName);
        if (po == null) return Optional.empty();

        return Optional.of(UserLocationTagEntity.builder()
                                                .userLocationTagId(po.getUserLocationTagId())
                                                .userId(po.getUserId())
                                                .name(po.getName())
                                                .latitude(po.getLatitude())
                                                .longitude(po.getLongitude())
                                                .relatedLocationTagId(po.getRelatedLocationTagId())
                                                .build());
    }

    @Override
    public Optional<UserLocationTagEntity> findById(String userLocationTagId) {
        UserLocationTags po = userLocationTagsMapper.findById(userLocationTagId);
        if (po == null) return Optional.empty();
        return Optional.of(UserLocationTagEntity.builder()
                                                .userLocationTagId(po.getUserLocationTagId())
                                                .userId(po.getUserId())
                                                .name(po.getName())
                                                .latitude(po.getLatitude())
                                                .longitude(po.getLongitude())
                                                .relatedLocationTagId(po.getRelatedLocationTagId())
                                                .note(po.getNote())
                                                .build());
    }

    @Override
    public List<UserLocationTagEntity> findByUserId(String userId) {
        List<UserLocationTags> pos = userLocationTagsMapper.findByUserId(userId);

        return pos.stream()
                  .map(po -> UserLocationTagEntity.builder()
                                                  .userLocationTagId(po.getUserLocationTagId())
                                                  .userId(po.getUserId())
                                                  .name(po.getName())
                                                  .latitude(po.getLatitude())
                                                  .longitude(po.getLongitude())
                                                  .relatedLocationTagId(po.getRelatedLocationTagId())
                                                  .note(po.getNote())
                                                  .createdAt(po.getCreatedAt() != null ? po.getCreatedAt().toString() : null)
                                                  .build())
                  .toList();
    }
}
