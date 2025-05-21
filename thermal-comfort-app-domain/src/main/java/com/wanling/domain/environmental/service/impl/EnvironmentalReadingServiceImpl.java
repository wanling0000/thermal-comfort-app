package com.wanling.domain.environmental.service.impl;

import java.util.List;

import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.repository.IEnvironmentalReadingRepository;
import com.wanling.domain.environmental.service.IEnvironmentReadingService;
import com.wanling.domain.environmental.service.ILocationTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvironmentalReadingServiceImpl implements IEnvironmentReadingService {

    private final ILocationTagService locationTagService;
    private final IEnvironmentalReadingRepository environmentalReadingRepository;

    @Override
    public void uploadReadings(List<EnvironmentalReadingEntity> readings) {
        for (EnvironmentalReadingEntity reading : readings) {
            LocationCandidateVO location = reading.getLocation();

            if (location == null) {
                reading.setLocationTagId(null);
                continue;
            }

            String locationTagId = locationTagService.resolveLocationTag(location); // 归一化
            reading.setLocationTagId(locationTagId);
        }

        environmentalReadingRepository.saveEnvironmentalReadings(readings);
    }

}
