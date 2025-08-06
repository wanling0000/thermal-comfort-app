package com.wanling.test.domain.feedback;

import java.util.List;

import javax.annotation.Resource;

import com.wanling.domain.environmental.model.entity.ComfortFeedbackEntity;
import com.wanling.domain.environmental.model.valobj.ComfortFeedbackWithReadingVO;
import com.wanling.domain.environmental.repository.IComfortFeedbackRepository;
import com.wanling.domain.environmental.service.IComfortFeedbackService;
import com.wanling.trigger.api.dto.ComfortFeedbackResponseDTO;
import com.wanling.trigger.assembler.ComfortFeedbackAssembler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
public class ComfortFeedbackServiceTest {
    @Resource
    private IComfortFeedbackService service;

    @Resource
    private IComfortFeedbackRepository repository;
    @Test
    void shouldReturnAllStructuredFeedbacks() {
        String userId = "8d468f7b-03f6-4760-a4b8-220e175b232c";
        List<ComfortFeedbackWithReadingVO> list = service.getFeedbackByMonth(2025, 5, userId);

        for (ComfortFeedbackWithReadingVO e : list) {
            System.out.printf(
                    "📌 ID=%s | user=%s | time=%s | temp=%d | humi=%d | level=%d | type=%s | act=%s | clothes=%s | adjTemp=%s | adjHumid=%s | notes=%s | lat=%.5f | lon=%.5f | location=%s | custom=%b | customTag=%s%n",
                    e.getFeedbackId(),
                    e.getUserId(),
                    e.getTimestamp(),
                    e.getTemperature(),
                    e.getHumidity(),
                    e.getComfortLevel(),
                    e.getFeedbackType(),
                    e.getActivityTypeId().orElse("-"),
                    e.getClothingLevel().orElse("-"),
                    e.getAdjustedTempLevel().map(String::valueOf).orElse("-"),
                    e.getAdjustedHumidLevel().map(String::valueOf).orElse("-"),
                    e.getNotes().orElse("-"),
                    e.getRawLatitude().orElse(0.0),
                    e.getRawLongitude().orElse(0.0),
                    e.getLocationDisplayName(),
                    e.isCustomLocation(),
                    e.getCustomTagName().orElse("-")
            );

            assertNotNull(e.getFeedbackId());
        }
    }


//    @Test
//    void shouldConvertEntityToResponseDTOAndPrint() {
//        String userId = "8d468f7b-03f6-4760-a4b8-220e175b232c";
//        List<ComfortFeedbackWithReadingVO> entities = service.getFeedbackByMonth(2025, 7, userId);
//        List<ComfortFeedbackResponseDTO> dtos = entities.stream()
//                                                        .map(ComfortFeedbackAssembler::toResponseDTO)
//                                                        .toList();
//
//        for (ComfortFeedbackResponseDTO dto : dtos) {
//            System.out.println("🎯 DTO = " + dto);
//        }
//
//        assertFalse(dtos.isEmpty());
//    }

}
