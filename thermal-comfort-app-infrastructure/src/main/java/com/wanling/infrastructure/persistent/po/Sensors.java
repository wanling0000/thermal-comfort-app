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
 * @TableName sensors
 */
@TableName(value ="sensors")
@Data
public class Sensors implements Serializable {
    /**
     * Primary key
     */
    @TableId
    private String sensor_id;

    /**
     * Sensor display name
     */
    private String name;

    /**
     * Sensor type, e.g., "SwitchBot"
     */
    private String type;

    /**
     * Timestamp of the latest data reading
     */
    private LocalDateTime lastReadingTime;

    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private String mac_address;

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
        Sensors other = (Sensors) that;
        return (this.getSensor_id() == null ? other.getSensor_id() == null : this.getSensor_id().equals(other.getSensor_id()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getLastReadingTime() == null ? other.getLastReadingTime() == null : this.getLastReadingTime().equals(other.getLastReadingTime()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMac_address() == null ? other.getMac_address() == null : this.getMac_address().equals(other.getMac_address()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSensor_id() == null) ? 0 : getSensor_id().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getLastReadingTime() == null) ? 0 : getLastReadingTime().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMac_address() == null) ? 0 : getMac_address().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sensor_id=").append(sensor_id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", last_reading_time=").append(lastReadingTime);
        sb.append(", user_id=").append(userId);
        sb.append(", mac_address=").append(mac_address);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}