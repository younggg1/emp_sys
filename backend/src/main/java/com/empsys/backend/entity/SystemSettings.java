package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("system_settings")
@Data
public class SystemSettings {
    @TableId(type = IdType.INPUT)
    private Long settingId;
    
    private Long schoolId;
    
    private Boolean requireCaptcha;
    
    private Boolean requireApproval;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
