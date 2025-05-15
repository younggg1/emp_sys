package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("permissions")
@Data
public class Permission {
    @TableId(type = IdType.INPUT)
    private Long permissionId;
    
    private Long userId;
    
    private Boolean canEdit;
    
    private Boolean canDelete;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
