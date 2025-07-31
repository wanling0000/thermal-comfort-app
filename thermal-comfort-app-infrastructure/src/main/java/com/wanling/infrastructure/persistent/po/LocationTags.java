package com.wanling.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName location_tags
 */
@TableName(value ="location_tags")
@Data
public class LocationTags implements Serializable {
    /**
     * 
     */
    @TableId
    private String locationTagId;

    /**
     * 
     */
    private String displayName;

    /**
     * 
     */
    private Double latitude;

    /**
     * 
     */
    private Double longitude;

    /**
     * 
     */
    private String coordinates;

    /**
     * 
     */
    private Boolean isCustom;

    /**
     * 
     */
    private LocalDateTime createdAt;

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
        LocationTags other = (LocationTags) that;
        return (this.getLocationTagId() == null ? other.getLocationTagId() == null : this.getLocationTagId().equals(other.getLocationTagId()))
            && (this.getDisplayName() == null ? other.getDisplayName() == null : this.getDisplayName().equals(other.getDisplayName()))
            && (this.getLatitude() == null ? other.getLatitude() == null : this.getLatitude().equals(other.getLatitude()))
            && (this.getLongitude() == null ? other.getLongitude() == null : this.getLongitude().equals(other.getLongitude()))
            && (this.getCoordinates() == null ? other.getCoordinates() == null : this.getCoordinates().equals(other.getCoordinates()))
            && (this.getIsCustom() == null ? other.getIsCustom() == null : this.getIsCustom().equals(other.getIsCustom()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getLocationTagId() == null) ? 0 : getLocationTagId().hashCode());
        result = prime * result + ((getDisplayName() == null) ? 0 : getDisplayName().hashCode());
        result = prime * result + ((getLatitude() == null) ? 0 : getLatitude().hashCode());
        result = prime * result + ((getLongitude() == null) ? 0 : getLongitude().hashCode());
        result = prime * result + ((getCoordinates() == null) ? 0 : getCoordinates().hashCode());
        result = prime * result + ((getIsCustom() == null) ? 0 : getIsCustom().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", location_tag_id=").append(locationTagId);
        sb.append(", display_name=").append(displayName);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", coordinates=").append(coordinates);
        sb.append(", is_custom=").append(isCustom);
        sb.append(", created_at=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}