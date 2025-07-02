package com.wanling.trigger.assembler;

import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.model.valobj.LocationPreviewVO;
import com.wanling.trigger.api.dto.LocationDTO;
import com.wanling.trigger.api.dto.LocationPreviewDTO;

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

    public static LocationPreviewDTO toDTO(LocationPreviewVO vo) {
        return new LocationPreviewDTO(
                vo.displayName(),
                vo.isCustom(),
                vo.customTag(),
                vo.latitude(),
                vo.longitude()
        );
    }
}
