package com.wanling.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
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
     * Primary key
     */
    @TableId
    private String feedback_id;

    /**
     * Associated user
     */
    private String user_id;

    /**
     * Feedback time
     */
    private LocalDateTime timestamp;

    /**
     * Scale -2 (too cold) to +2 (too hot)
     */
    private Integer comfort_level;

    /**
     * Quick vs detailed
     */
    private String feedback_type;

    /**
     * FK to Activity
     */
    private String activity_type_id;

    /**
     * One of "light", "medium", "heavy"
     */
    private String clothing_level;

    /**
     * Desired change to temp, e.g., -1 = want cooler
     */
    private Integer adjusted_temp_level;

    /**
     * Desired change to humidity
     */
    private Integer adjusted_humid_level;

    /**
     * Freeform user notes
     */
    private String notes;

    /**
     * Location of feedback
     */
    private String location_tag_id;

    /**
     * FK to associated environmental reading
     */
    private String reading_id;

    /**
     * Actual location when feedback was submitted
     */
    private String raw_coordinates;

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
        return (this.getFeedback_id() == null ? other.getFeedback_id() == null : this.getFeedback_id().equals(other.getFeedback_id()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()))
            && (this.getComfort_level() == null ? other.getComfort_level() == null : this.getComfort_level().equals(other.getComfort_level()))
            && (this.getFeedback_type() == null ? other.getFeedback_type() == null : this.getFeedback_type().equals(other.getFeedback_type()))
            && (this.getActivity_type_id() == null ? other.getActivity_type_id() == null : this.getActivity_type_id().equals(other.getActivity_type_id()))
            && (this.getClothing_level() == null ? other.getClothing_level() == null : this.getClothing_level().equals(other.getClothing_level()))
            && (this.getAdjusted_temp_level() == null ? other.getAdjusted_temp_level() == null : this.getAdjusted_temp_level().equals(other.getAdjusted_temp_level()))
            && (this.getAdjusted_humid_level() == null ? other.getAdjusted_humid_level() == null : this.getAdjusted_humid_level().equals(other.getAdjusted_humid_level()))
            && (this.getNotes() == null ? other.getNotes() == null : this.getNotes().equals(other.getNotes()))
            && (this.getLocation_tag_id() == null ? other.getLocation_tag_id() == null : this.getLocation_tag_id().equals(other.getLocation_tag_id()))
            && (this.getReading_id() == null ? other.getReading_id() == null : this.getReading_id().equals(other.getReading_id()))
            && (this.getRaw_coordinates() == null ? other.getRaw_coordinates() == null : this.getRaw_coordinates().equals(other.getRaw_coordinates()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFeedback_id() == null) ? 0 : getFeedback_id().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        result = prime * result + ((getComfort_level() == null) ? 0 : getComfort_level().hashCode());
        result = prime * result + ((getFeedback_type() == null) ? 0 : getFeedback_type().hashCode());
        result = prime * result + ((getActivity_type_id() == null) ? 0 : getActivity_type_id().hashCode());
        result = prime * result + ((getClothing_level() == null) ? 0 : getClothing_level().hashCode());
        result = prime * result + ((getAdjusted_temp_level() == null) ? 0 : getAdjusted_temp_level().hashCode());
        result = prime * result + ((getAdjusted_humid_level() == null) ? 0 : getAdjusted_humid_level().hashCode());
        result = prime * result + ((getNotes() == null) ? 0 : getNotes().hashCode());
        result = prime * result + ((getLocation_tag_id() == null) ? 0 : getLocation_tag_id().hashCode());
        result = prime * result + ((getReading_id() == null) ? 0 : getReading_id().hashCode());
        result = prime * result + ((getRaw_coordinates() == null) ? 0 : getRaw_coordinates().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", feedback_id=").append(feedback_id);
        sb.append(", user_id=").append(user_id);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", comfort_level=").append(comfort_level);
        sb.append(", feedback_type=").append(feedback_type);
        sb.append(", activity_type_id=").append(activity_type_id);
        sb.append(", clothing_level=").append(clothing_level);
        sb.append(", adjusted_temp_level=").append(adjusted_temp_level);
        sb.append(", adjusted_humid_level=").append(adjusted_humid_level);
        sb.append(", notes=").append(notes);
        sb.append(", location_tag_id=").append(location_tag_id);
        sb.append(", reading_id=").append(reading_id);
        sb.append(", raw_coordinates=").append(raw_coordinates);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}