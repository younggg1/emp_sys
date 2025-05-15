package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("employment_records")
@Data
public class EmploymentRecord {
    @TableId(type = IdType.INPUT)
    private Long recordId;
    
    private Long studentId;
    
    private String name;
    
    private String major;
    
    private String majorCategory; // 'science', 'humanities', 'sports', 'arts'
    
    private String companyNature;
    
    private String company;
    
    private String position;
    
    private BigDecimal salary;
    
    private LocalDate entryDate;
    
    private String region;
    
    private String status; // 'pending', 'approved'
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
