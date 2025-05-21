package com.wanling.trigger.api;

import java.util.List;

import com.wanling.trigger.api.dto.EnvironmentalReadingDTO;
import com.wanling.types.model.Response;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 08/05/2025
 * 22:22
 */
public interface IEnvironmentReading {
    Response<String> uploadReadings(List<EnvironmentalReadingDTO> dtoList);
}
