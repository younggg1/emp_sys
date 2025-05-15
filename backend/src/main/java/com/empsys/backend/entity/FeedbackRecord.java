package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("feedback_records")
@Data
public class FeedbackRecord {
    @TableId(type = IdType.INPUT)
    private Long feedbackId;
    
    private Long studentId;
    
    private String stage;
    
    private String content;
    
    private String status; // 'pending', 'approved'
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
