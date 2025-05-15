package com.empsys.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.entity.FeedbackRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedbackMapper extends BaseMapper<FeedbackRecord> {
    
    /**
     * 获取指定学生的反馈记录
     * @param studentId 学生ID
     * @return 反馈记录列表
     */
    List<FeedbackRecord> getFeedbackByStudent(@Param("studentId") Long studentId);
    
    /**
     * 获取指定辅导员管理的学生的反馈记录
     * @param page 分页参数
     * @param counselorId 辅导员ID
     * @return 反馈记录分页数据
     */
    IPage<FeedbackRecord> getFeedbackByCounselor(Page<FeedbackRecord> page, @Param("counselorId") Long counselorId);
    
    /**
     * 获取指定学校的反馈记录
     * @param page 分页参数
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @return 反馈记录分页数据
     */
    IPage<FeedbackRecord> getFeedbackBySchool(Page<FeedbackRecord> page, @Param("schoolId") Long schoolId, @Param("year") Integer year);
} 
