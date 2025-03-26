package com.wanling.backend.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wanling.backend.entity.BleData;
import com.wanling.backend.mapper.BleDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BleDataService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private BleDataMapper bleDataMapper;

    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    private static final String HISTORY_KEY = "ble:history";
    private static final int MAX_READ = 100;

    @Scheduled(fixedRate = 10000) // 每5秒执行一次
    public void flushBleDataToDatabase() {
        List<String> jsonList = redisTemplate.opsForList().range(HISTORY_KEY, 0, MAX_READ - 1);
        if (jsonList == null || jsonList.isEmpty()) {
            return;
        }

        List<BleData> dataList = new ArrayList<>();
        for (String json : jsonList) {
            try {
                BleData data = objectMapper.readValue(json, BleData.class);
                dataList.add(data);
            } catch (Exception e) {
                e.printStackTrace();

                // log or ignore
            }
        }

        // 批量插入
        for (BleData data : dataList) {
            bleDataMapper.insert(data);
        }

        // 清空 Redis 里的历史数据（可选，避免重复写入）
        redisTemplate.delete(HISTORY_KEY);

        System.out.println("✅ Flushed " + dataList.size() + " records to database");
    }
}