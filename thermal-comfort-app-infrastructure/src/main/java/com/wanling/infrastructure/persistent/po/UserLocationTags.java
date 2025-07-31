package com.wanling.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName user_location_tags
 */
@TableName(value ="user_location_tags")
@Data
public class UserLocationTags implements Serializable {
    /**
     * 
     */
    @TableId
    private String userLocationTagId;

    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private String name;

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
    @TableField(exist = false)
    private String coordinates;

    /**
     * 
     */
    private String relatedLocationTagId;

    /**
     * 
     */
    private String note;

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
        UserLocationTags other = (UserLocationTags) that;
        return (this.getUserLocationTagId() == null ? other.getUserLocationTagId() == null : this.getUserLocationTagId().equals(other.getUserLocationTagId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getLatitude() == null ? other.getLatitude() == null : this.getLatitude().equals(other.getLatitude()))
            && (this.getLongitude() == null ? other.getLongitude() == null : this.getLongitude().equals(other.getLongitude()))
            && (this.getCoordinates() == null ? other.getCoordinates() == null : this.getCoordinates().equals(other.getCoordinates()))
            && (this.getRelatedLocationTagId() == null ? other.getRelatedLocationTagId() == null : this.getRelatedLocationTagId().equals(other.getRelatedLocationTagId()))
            && (this.getNote() == null ? other.getNote() == null : this.getNote().equals(other.getNote()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserLocationTagId() == null) ? 0 : getUserLocationTagId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getLatitude() == null) ? 0 : getLatitude().hashCode());
        result = prime * result + ((getLongitude() == null) ? 0 : getLongitude().hashCode());
        result = prime * result + ((getCoordinates() == null) ? 0 : getCoordinates().hashCode());
        result = prime * result + ((getRelatedLocationTagId() == null) ? 0 : getRelatedLocationTagId().hashCode());
        result = prime * result + ((getNote() == null) ? 0 : getNote().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", user_location_tag_id=").append(userLocationTagId);
        sb.append(", user_id=").append(userId);
        sb.append(", name=").append(name);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", coordinates=").append(coordinates);
        sb.append(", related_location_tag_id=").append(relatedLocationTagId);
        sb.append(", note=").append(note);
        sb.append(", created_at=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}