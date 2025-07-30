package com.wanling.domain.environmental.service.impl;

import java.util.List;

import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.repository.IEnvironmentalReadingRepository;
import com.wanling.domain.environmental.service.IEnvironmentReadingService;
import com.wanling.domain.environmental.service.ILocationTagService;
import com.wanling.domain.environmental.service.IUserLocationService;
import com.wanling.types.security.LoginUserHolder;
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
        String userId = LoginUserHolder.get().userId();
        for (EnvironmentalReadingEntity reading : readings) {
            reading.setUserId(userId);
            LocationCandidateVO location = reading.getLocation();

            if (location == null) {
                reading.setLocationTagId(null);
                continue;
            }

            String locationTagId = locationTagService.normalizeAndFindOrCreate(location, userId);
            reading.setLocationTagId(locationTagId);
        }

        environmentalReadingRepository.saveEnvironmentalReadings(readings);
    }

}
