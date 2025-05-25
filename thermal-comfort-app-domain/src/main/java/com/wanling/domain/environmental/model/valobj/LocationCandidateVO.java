package com.wanling.domain.environmental.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 20/05/2025
 * 11:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationCandidateVO {
    private String displayName;
    private Double latitude;
    private Double longitude;
    private Boolean isCustom;
    private String customTag; // 对于 isCustom=true 的情况

    public Boolean isCustom() {
        return isCustom;
    }
}
