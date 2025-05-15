package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("students")
@Data
public class Student {
    @TableId(type = IdType.INPUT)
    private Long studentId;
    
    private Long schoolId;
    
    private String name;
    
    private String className;
    
    private String college;
    
    private String major;
    
    private Long counselorId;
    
    private String employmentStatus; // 'unemployed', 'employed'
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
