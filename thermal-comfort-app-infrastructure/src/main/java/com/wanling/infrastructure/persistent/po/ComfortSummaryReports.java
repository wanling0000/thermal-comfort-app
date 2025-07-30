package com.wanling.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName comfort_summary_reports
 */
@TableName(value ="comfort_summary_reports")
@Data
public class ComfortSummaryReports implements Serializable {
    /**
     * 
     */
    @TableId
    private String summary_id;

    /**
     * 
     */
    private String user_id;

    /**
     * 
     */
    private LocalDate start_date;

    /**
     * 
     */
    private LocalDate end_date;

    /**
     * 
     */
    private Object temp_comfort_range;

    /**
     * 
     */
    private Object location_stats;

    /**
     * 
     */
    private LocalDateTime created_at;

    /**
     * 
     */
    private String generated_by;

    /**
     * 
     */
    private String source_type;

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
        ComfortSummaryReports other = (ComfortSummaryReports) that;
        return (this.getSummary_id() == null ? other.getSummary_id() == null : this.getSummary_id().equals(other.getSummary_id()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getStart_date() == null ? other.getStart_date() == null : this.getStart_date().equals(other.getStart_date()))
            && (this.getEnd_date() == null ? other.getEnd_date() == null : this.getEnd_date().equals(other.getEnd_date()))
            && (this.getTemp_comfort_range() == null ? other.getTemp_comfort_range() == null : this.getTemp_comfort_range().equals(other.getTemp_comfort_range()))
            && (this.getLocation_stats() == null ? other.getLocation_stats() == null : this.getLocation_stats().equals(other.getLocation_stats()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getGenerated_by() == null ? other.getGenerated_by() == null : this.getGenerated_by().equals(other.getGenerated_by()))
            && (this.getSource_type() == null ? other.getSource_type() == null : this.getSource_type().equals(other.getSource_type()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSummary_id() == null) ? 0 : getSummary_id().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getStart_date() == null) ? 0 : getStart_date().hashCode());
        result = prime * result + ((getEnd_date() == null) ? 0 : getEnd_date().hashCode());
        result = prime * result + ((getTemp_comfort_range() == null) ? 0 : getTemp_comfort_range().hashCode());
        result = prime * result + ((getLocation_stats() == null) ? 0 : getLocation_stats().hashCode());
        result = prime * result + ((getCreated_at() == null) ? 0 : getCreated_at().hashCode());
        result = prime * result + ((getGenerated_by() == null) ? 0 : getGenerated_by().hashCode());
        result = prime * result + ((getSource_type() == null) ? 0 : getSource_type().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", summary_id=").append(summary_id);
        sb.append(", user_id=").append(user_id);
        sb.append(", start_date=").append(start_date);
        sb.append(", end_date=").append(end_date);
        sb.append(", temp_comfort_range=").append(temp_comfort_range);
        sb.append(", location_stats=").append(location_stats);
        sb.append(", created_at=").append(created_at);
        sb.append(", generated_by=").append(generated_by);
        sb.append(", source_type=").append(source_type);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}