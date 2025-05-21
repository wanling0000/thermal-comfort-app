package com.wanling.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    /**
     * Actual GPS coordinates where the reading occurred
     */
    private String rawCoordinates;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        EnvironmentalReadings other = (EnvironmentalReadings) that;
        return (this.getReadingId() == null ? other.getReadingId() == null : this.getReadingId().equals(other.getReadingId()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()))
            && (this.getTemperature() == null ? other.getTemperature() == null : this.getTemperature().equals(other.getTemperature()))
            && (this.getHumidity() == null ? other.getHumidity() == null : this.getHumidity().equals(other.getHumidity()))
            && (this.getSensorId() == null ? other.getSensorId() == null : this.getSensorId().equals(other.getSensorId()))
            && (this.getLocationTagId() == null ? other.getLocationTagId() == null : this.getLocationTagId().equals(other.getLocationTagId()))
            && (this.getRawCoordinates() == null ? other.getRawCoordinates() == null : this.getRawCoordinates().equals(other.getRawCoordinates()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getReadingId() == null) ? 0 : getReadingId().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        result = prime * result + ((getTemperature() == null) ? 0 : getTemperature().hashCode());
        result = prime * result + ((getHumidity() == null) ? 0 : getHumidity().hashCode());
        result = prime * result + ((getSensorId() == null) ? 0 : getSensorId().hashCode());
        result = prime * result + ((getLocationTagId() == null) ? 0 : getLocationTagId().hashCode());
        result = prime * result + ((getRawCoordinates() == null) ? 0 : getRawCoordinates().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", reading_id=").append(readingId);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", temperature=").append(temperature);
        sb.append(", humidity=").append(humidity);
        sb.append(", sensor_id=").append(sensorId);
        sb.append(", location_tag_id=").append(locationTagId);
        sb.append(", raw_coordinates=").append(rawCoordinates);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}