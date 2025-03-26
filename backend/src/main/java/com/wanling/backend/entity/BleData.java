package com.wanling.backend.entity;

import java.time.LocalDateTime;

public class BleData {
    private Long id;
    private Double temperature;
    private Integer humidity;
    private LocalDateTime timestamp;

    public BleData() {}

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
