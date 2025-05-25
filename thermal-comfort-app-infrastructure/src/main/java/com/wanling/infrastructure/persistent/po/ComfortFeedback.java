package com.wanling.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName comfort_feedback
 */
@TableName(value ="comfort_feedback")
@Data
public class ComfortFeedback implements Serializable {
    /**
     * 
     */
    @TableId
    private String feedbackId;

    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private LocalDateTime timestamp;

    /**
     * 
     */
    private Integer comfortLevel;

    /**
     * 
     */
    private String feedbackType;

    /**
     * 
     */
    private String activityTypeId;

    /**
     * 
     */
    private String clothingLevel;

    /**
     * 
     */
    private Integer adjustedTempLevel;

    /**
     * 
     */
    private Integer adjustedHumidLevel;

    /**
     * 
     */
    private String notes;

    /**
     * 
     */
    private String locationTagId;

    /**
     * 
     */
    private String readingId;

    /**
     * 
     */
    private Object rawCoordinates;

    /**
     * 
     */
    private String userLocationTagId;

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
        ComfortFeedback other = (ComfortFeedback) that;
        return (this.getFeedbackId() == null ? other.getFeedbackId() == null : this.getFeedbackId().equals(other.getFeedbackId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()))
            && (this.getComfortLevel() == null ? other.getComfortLevel() == null : this.getComfortLevel().equals(other.getComfortLevel()))
            && (this.getFeedbackType() == null ? other.getFeedbackType() == null : this.getFeedbackType().equals(other.getFeedbackType()))
            && (this.getActivityTypeId() == null ? other.getActivityTypeId() == null : this.getActivityTypeId().equals(other.getActivityTypeId()))
            && (this.getClothingLevel() == null ? other.getClothingLevel() == null : this.getClothingLevel().equals(other.getClothingLevel()))
            && (this.getAdjustedTempLevel() == null ? other.getAdjustedTempLevel() == null : this.getAdjustedTempLevel().equals(other.getAdjustedTempLevel()))
            && (this.getAdjustedHumidLevel() == null ? other.getAdjustedHumidLevel() == null : this.getAdjustedHumidLevel().equals(other.getAdjustedHumidLevel()))
            && (this.getNotes() == null ? other.getNotes() == null : this.getNotes().equals(other.getNotes()))
            && (this.getLocationTagId() == null ? other.getLocationTagId() == null : this.getLocationTagId().equals(other.getLocationTagId()))
            && (this.getReadingId() == null ? other.getReadingId() == null : this.getReadingId().equals(other.getReadingId()))
            && (this.getRawCoordinates() == null ? other.getRawCoordinates() == null : this.getRawCoordinates().equals(other.getRawCoordinates()))
            && (this.getUserLocationTagId() == null ? other.getUserLocationTagId() == null : this.getUserLocationTagId().equals(other.getUserLocationTagId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFeedbackId() == null) ? 0 : getFeedbackId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        result = prime * result + ((getComfortLevel() == null) ? 0 : getComfortLevel().hashCode());
        result = prime * result + ((getFeedbackType() == null) ? 0 : getFeedbackType().hashCode());
        result = prime * result + ((getActivityTypeId() == null) ? 0 : getActivityTypeId().hashCode());
        result = prime * result + ((getClothingLevel() == null) ? 0 : getClothingLevel().hashCode());
        result = prime * result + ((getAdjustedTempLevel() == null) ? 0 : getAdjustedTempLevel().hashCode());
        result = prime * result + ((getAdjustedHumidLevel() == null) ? 0 : getAdjustedHumidLevel().hashCode());
        result = prime * result + ((getNotes() == null) ? 0 : getNotes().hashCode());
        result = prime * result + ((getLocationTagId() == null) ? 0 : getLocationTagId().hashCode());
        result = prime * result + ((getReadingId() == null) ? 0 : getReadingId().hashCode());
        result = prime * result + ((getRawCoordinates() == null) ? 0 : getRawCoordinates().hashCode());
        result = prime * result + ((getUserLocationTagId() == null) ? 0 : getUserLocationTagId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", feedback_id=").append(feedbackId);
        sb.append(", user_id=").append(userId);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", comfort_level=").append(comfortLevel);
        sb.append(", feedback_type=").append(feedbackType);
        sb.append(", activity_type_id=").append(activityTypeId);
        sb.append(", clothing_level=").append(clothingLevel);
        sb.append(", adjusted_temp_level=").append(adjustedTempLevel);
        sb.append(", adjusted_humid_level=").append(adjustedHumidLevel);
        sb.append(", notes=").append(notes);
        sb.append(", location_tag_id=").append(locationTagId);
        sb.append(", reading_id=").append(readingId);
        sb.append(", raw_coordinates=").append(rawCoordinates);
        sb.append(", user_location_tag_id=").append(userLocationTagId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}