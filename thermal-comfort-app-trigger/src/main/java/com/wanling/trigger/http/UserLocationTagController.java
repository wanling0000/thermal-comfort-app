package com.wanling.trigger.http;

import java.util.List;
import java.util.Optional;

import com.alibaba.fastjson.JSON;
import com.wanling.domain.environmental.model.entity.UserLocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;
import com.wanling.domain.environmental.model.valobj.LocationPreviewVO;
import com.wanling.domain.environmental.service.IUserLocationService;
import com.wanling.trigger.api.dto.LocationPreviewDTO;
import com.wanling.trigger.api.dto.UserLocationTagCreateDTO;
import com.wanling.trigger.assembler.LocationAssembler;
import com.wanling.types.enums.ResponseCode;
import com.wanling.types.model.Response;
import com.wanling.types.security.LoginUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/user-location-tags")
public class UserLocationTagController {
    private final IUserLocationService userLocationService;

    @GetMapping("/preview")
    public List<LocationPreviewDTO> getPreview() {
        String userId = LoginUserHolder.get().userId();
        return userLocationService.findAllByUserId(userId)
                                  .stream()
                                  .map(LocationAssembler::toDTO)
                                  .toList();
    }

    @PostMapping("/create")
    public Response<String> createUserLocationTag(@RequestBody UserLocationTagCreateDTO dto) {
        log.debug("📍 Creating user tag: {}", JSON.toJSONString(dto));

        LocationCandidateVO location = (dto.getLocation() != null)
            ? LocationAssembler.toCandidate(dto.getLocation())
            : null;

        if (location != null) {
            location.setIsCustom(true);       // 明确标记为自定义位置
            location.setCustomTag(dto.getName());  // 使用用户命名的 tag 名
        }

        String userId = LoginUserHolder.get().userId();

        String tagId = userLocationService.createCustomTag(
                userId,
                dto.getName(),
                Optional.ofNullable(location));

        return Response.<String>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("Created user location tag")
                       .data(tagId)
                       .build();
    }
}
