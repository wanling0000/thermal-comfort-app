package com.wanling.trigger.assembler;

import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.trigger.api.dto.LocationDTO;
public class LocationAssembler {

    public static LocationCandidateVO toCandidate(LocationDTO dto) {
        return LocationCandidateVO.builder()
                                  .displayName(dto.displayName())
                                  .latitude(dto.latitude())
                                  .longitude(dto.longitude())
                                  .isCustom(Boolean.TRUE.equals(dto.isCustom()))
                                  .customTag(dto.customTag())
                                  .build();
    }
}
