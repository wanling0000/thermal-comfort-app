package com.wanling.trigger.assembler;

import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.trigger.api.dto.LocationDTO;
public class LocationAssembler {

    public static LocationCandidateVO toCandidate(LocationDTO dto) {
        return LocationCandidateVO.builder()
                                .displayName(dto.getDisplayName())
                                .latitude(dto.getLatitude())
                                .longitude(dto.getLongitude())
                                .isCustom(Boolean.TRUE.equals(dto.getIsCustom()))
                                .customTag(dto.getCustomTag())
                                .build();
    }
}
