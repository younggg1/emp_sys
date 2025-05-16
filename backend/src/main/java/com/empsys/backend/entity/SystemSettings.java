package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 系统设置表实体类
 * @TableName system_settings
 */
@TableName(value ="system_settings")
@Data
public class SystemSettings {
    /**
     * 设置ID
     */
    @TableId(value = "setting_id", type = IdType.AUTO)
    private Long settingId;

    /**
     * 验证码开关
     */
    @TableField(value = "require_captcha")
    private Boolean requireCaptcha;

    /**
     * 审核开关
     */
    @TableField(value = "require_approval")
    private Boolean requireApproval;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private Date updatedAt;

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
        SystemSettings other = (SystemSettings) that;
        return (this.getSettingId() == null ? other.getSettingId() == null : this.getSettingId().equals(other.getSettingId()))
            && (this.getRequireCaptcha() == null ? other.getRequireCaptcha() == null : this.getRequireCaptcha().equals(other.getRequireCaptcha()))
            && (this.getRequireApproval() == null ? other.getRequireApproval() == null : this.getRequireApproval().equals(other.getRequireApproval()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSettingId() == null) ? 0 : getSettingId().hashCode());
        result = prime * result + ((getRequireCaptcha() == null) ? 0 : getRequireCaptcha().hashCode());
        result = prime * result + ((getRequireApproval() == null) ? 0 : getRequireApproval().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", settingId=").append(settingId);
        sb.append(", requireCaptcha=").append(requireCaptcha);
        sb.append(", requireApproval=").append(requireApproval);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
} 