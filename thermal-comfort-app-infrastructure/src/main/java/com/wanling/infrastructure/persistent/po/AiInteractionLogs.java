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
 * @TableName ai_interaction_logs
 */
@TableName(value ="ai_interaction_logs")
@Data
public class AiInteractionLogs implements Serializable {
    /**
     * 
     */
    @TableId
    private String log_id;

    /**
     * 
     */
    private String user_id;

    /**
     * 
     */
    private LocalDateTime timestamp;

    /**
     * 
     */
    private String question;

    /**
     * 
     */
    private String response;

    /**
     * 
     */
    private String used_summary_id;

    /**
     * 
     */
    private String response_type;

    /**
     * 
     */
    private String used_model;

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
        AiInteractionLogs other = (AiInteractionLogs) that;
        return (this.getLog_id() == null ? other.getLog_id() == null : this.getLog_id().equals(other.getLog_id()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()))
            && (this.getQuestion() == null ? other.getQuestion() == null : this.getQuestion().equals(other.getQuestion()))
            && (this.getResponse() == null ? other.getResponse() == null : this.getResponse().equals(other.getResponse()))
            && (this.getUsed_summary_id() == null ? other.getUsed_summary_id() == null : this.getUsed_summary_id().equals(other.getUsed_summary_id()))
            && (this.getResponse_type() == null ? other.getResponse_type() == null : this.getResponse_type().equals(other.getResponse_type()))
            && (this.getUsed_model() == null ? other.getUsed_model() == null : this.getUsed_model().equals(other.getUsed_model()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getLog_id() == null) ? 0 : getLog_id().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        result = prime * result + ((getQuestion() == null) ? 0 : getQuestion().hashCode());
        result = prime * result + ((getResponse() == null) ? 0 : getResponse().hashCode());
        result = prime * result + ((getUsed_summary_id() == null) ? 0 : getUsed_summary_id().hashCode());
        result = prime * result + ((getResponse_type() == null) ? 0 : getResponse_type().hashCode());
        result = prime * result + ((getUsed_model() == null) ? 0 : getUsed_model().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", log_id=").append(log_id);
        sb.append(", user_id=").append(user_id);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", question=").append(question);
        sb.append(", response=").append(response);
        sb.append(", used_summary_id=").append(used_summary_id);
        sb.append(", response_type=").append(response_type);
        sb.append(", used_model=").append(used_model);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}