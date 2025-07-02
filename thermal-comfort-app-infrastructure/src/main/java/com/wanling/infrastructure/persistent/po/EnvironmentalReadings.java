package com.wanling.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * @TableName environmental_readings
 */
@TableName(value ="environmental_readings")
@Data
public class EnvironmentalReadings implements Serializable {
    /**
     * Primary key
     */
    @TableId
    private String readingId;

    /**
     * When the reading was taken
     */
    private LocalDateTime timestamp;

    /**
     * Temperature value in Celsius
     */
    private Double temperature;

    /**
     * Relative humidity in %
     */
    private Double humidity;


    /**
     * Associated sensor
     */
    private String sensorId;

    /**
     * User-defined or default location tag
     */
    private String locationTagId;

    private String userId;

    /**
     * Actual GPS coordinates where the reading occurred
     */
    private String rawCoordinates;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EnvironmentalReadings that = (EnvironmentalReadings) o;
        return Objects.equals(readingId, that.readingId) && Objects.equals(timestamp, that.timestamp) && Objects.equals(temperature, that.temperature) && Objects.equals(humidity, that.humidity) && Objects.equals(sensorId, that.sensorId) && Objects.equals(locationTagId, that.locationTagId) && Objects.equals(userId, that.userId) && Objects.equals(rawCoordinates, that.rawCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readingId, timestamp, temperature, humidity, sensorId, locationTagId, userId, rawCoordinates);
    }

    @Override
    public String toString() {
        return "EnvironmentalReadings{" +
                "readingId='" + readingId + '\'' +
                ", timestamp=" + timestamp +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", sensorId='" + sensorId + '\'' +
                ", locationTagId='" + locationTagId + '\'' +
                ", userId='" + userId + '\'' +
                ", rawCoordinates='" + rawCoordinates + '\'' +
                '}';
    }
}