package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName feedback_records
 */
@TableName(value ="feedback_records")
@Data
public class FeedbackRecords {
    /**
     * 
     */
    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Long feedback_id;

    /**
     * 
     */
    @TableField(value = "student_id")
    private Long student_id;

    /**
     * 
     */
    @TableField(value = "stage")
    private String stage;

    /**
     * 
     */
    @TableField(value = "content")
    private String content;

    /**
     * 
     */
    @TableField(value = "status")
    private String status;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date created_at;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private Date updated_at;

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
        FeedbackRecords other = (FeedbackRecords) that;
        return (this.getFeedback_id() == null ? other.getFeedback_id() == null : this.getFeedback_id().equals(other.getFeedback_id()))
            && (this.getStudent_id() == null ? other.getStudent_id() == null : this.getStudent_id().equals(other.getStudent_id()))
            && (this.getStage() == null ? other.getStage() == null : this.getStage().equals(other.getStage()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFeedback_id() == null) ? 0 : getFeedback_id().hashCode());
        result = prime * result + ((getStudent_id() == null) ? 0 : getStudent_id().hashCode());
        result = prime * result + ((getStage() == null) ? 0 : getStage().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreated_at() == null) ? 0 : getCreated_at().hashCode());
        result = prime * result + ((getUpdated_at() == null) ? 0 : getUpdated_at().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", feedback_id=").append(feedback_id);
        sb.append(", student_id=").append(student_id);
        sb.append(", stage=").append(stage);
        sb.append(", content=").append(content);
        sb.append(", status=").append(status);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append("]");
        return sb.toString();
    }
}