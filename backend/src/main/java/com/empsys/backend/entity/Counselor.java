package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;


@TableName("counselors")
@Data
public class Counselor {
    @TableId(type = IdType.INPUT)
    private Long counselorId;
    
    private Long schoolId;
    
    private String name;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
