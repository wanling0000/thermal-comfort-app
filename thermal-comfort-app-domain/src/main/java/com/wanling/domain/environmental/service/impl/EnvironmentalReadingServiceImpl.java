package com.wanling.domain.environmental.service.impl;

import java.util.List;

import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.repository.IEnvironmentalReadingRepository;
import com.wanling.domain.environmental.service.IEnvironmentReadingService;
import com.wanling.domain.environmental.service.ILocationTagService;
import com.wanling.domain.environmental.service.IUserLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvironmentalReadingServiceImpl implements IEnvironmentReadingService {

    private final ILocationTagService locationTagService;
    private final IEnvironmentalReadingRepository environmentalReadingRepository;
    private final IUserLocationService userLocationService;

    @Override
    public void uploadReadings(List<EnvironmentalReadingEntity> readings) {
        for (EnvironmentalReadingEntity reading : readings) {
            LocationCandidateVO location = reading.getLocation();

            if (location == null) {
                reading.setLocationTagId(null);
                continue;
            }

            String locationTagId;
            if (location.getIsCustom() && location.getCustomTag() != null) {
                locationTagId = userLocationService.resolveToSystemTag(location.getCustomTag())
                                                   .map(LocationTagEntity::getLocationTagId)
                                                   .orElse(null);
            } else {
                locationTagId = locationTagService.findOrCreate(location);
            }
            reading.setLocationTagId(locationTagId);
        }

        environmentalReadingRepository.saveEnvironmentalReadings(readings);
    }

}
