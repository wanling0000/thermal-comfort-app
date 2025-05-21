package com.wanling.trigger.http;

/**
 * @Author
 * fwl
 * @Description
 * @Date
 * 05/05/2025
 * 22:32
 */

import java.util.List;


import com.alibaba.fastjson.JSON;
import com.wanling.domain.environmental.model.entity.EnvironmentalReadingEntity;
import com.wanling.domain.environmental.service.IEnvironmentReadingService;
import com.wanling.trigger.api.IEnvironmentReading;
import com.wanling.trigger.api.dto.EnvironmentalReadingDTO;
import com.wanling.trigger.assembler.EnvironmentalReadingAssembler;
import com.wanling.types.enums.ResponseCode;
import com.wanling.types.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/readings")
public class EnvironmentalReadingController implements IEnvironmentReading {
    private final IEnvironmentReadingService readingService;

    @PostMapping("/upload")
    public Response<String> uploadReadings(@RequestBody List<EnvironmentalReadingDTO> dtoList) {
        log.info("✅ Received readings from frontend: {}", JSON.toJSONString(dtoList));

        List<EnvironmentalReadingEntity> entities = dtoList.stream()
                                                           .map(EnvironmentalReadingAssembler::toEntity)
                                                           .toList();

        readingService.uploadReadings(entities);

        return Response.<String>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("Successfully received " + entities.size() + " readings")
                       .data("OK")
                       .build();
    }
}
