package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("users")
@Data
public class User {
    @TableId(type = IdType.INPUT)
    private Long userId;
    
    private Long schoolId;
    
    private String username;
    
    private String password;
    
    private String role; // 'student', 'counselor', 'admin'
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
