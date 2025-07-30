package com.wanling.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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
     * geometry(Point, 4326)
     */
    private String rawCoordinates;

    /**
     * 
     */
    private String userLocationTagId;

    @TableLogic
    private Boolean isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ComfortFeedback that = (ComfortFeedback) o;
        return Objects.equals(feedbackId, that.feedbackId) && Objects.equals(userId, that.userId) && Objects.equals(timestamp, that.timestamp) && Objects.equals(comfortLevel, that.comfortLevel) && Objects.equals(feedbackType, that.feedbackType) && Objects.equals(activityTypeId, that.activityTypeId) && Objects.equals(clothingLevel, that.clothingLevel) && Objects.equals(adjustedTempLevel, that.adjustedTempLevel) && Objects.equals(adjustedHumidLevel, that.adjustedHumidLevel) && Objects.equals(notes, that.notes) && Objects.equals(locationTagId, that.locationTagId) && Objects.equals(readingId, that.readingId) && Objects.equals(rawCoordinates, that.rawCoordinates) && Objects.equals(userLocationTagId, that.userLocationTagId) && Objects.equals(isDeleted, that.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackId, userId, timestamp, comfortLevel, feedbackType, activityTypeId, clothingLevel, adjustedTempLevel, adjustedHumidLevel, notes, locationTagId, readingId, rawCoordinates, userLocationTagId, isDeleted);
    }

    @Override
    public String toString() {
        return "ComfortFeedback{" +
                "feedbackId='" + feedbackId + '\'' +
                ", userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                ", comfortLevel=" + comfortLevel +
                ", feedbackType='" + feedbackType + '\'' +
                ", activityTypeId='" + activityTypeId + '\'' +
                ", clothingLevel='" + clothingLevel + '\'' +
                ", adjustedTempLevel=" + adjustedTempLevel +
                ", adjustedHumidLevel=" + adjustedHumidLevel +
                ", notes='" + notes + '\'' +
                ", locationTagId='" + locationTagId + '\'' +
                ", readingId='" + readingId + '\'' +
                ", rawCoordinates='" + rawCoordinates + '\'' +
                ", userLocationTagId='" + userLocationTagId + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}