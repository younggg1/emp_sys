package com.empsys.backend.service;

import com.empsys.backend.entity.FeedbackRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
* @author Quagmire
* @description 针对表【feedback_records】的数据库操作Service
* @createDate 2025-05-16 18:10:11
*/
public interface FeedbackRecordsService extends IService<FeedbackRecords> {

    /**
     * 获取学生的反馈列表
     * @param studentId 学生ID
     * @return 反馈列表
     */
    List<FeedbackRecords> getStudentFeedbackRecords(Long studentId);
    
    /**
     * 分页获取学生的反馈列表
     * @param page 分页参数
     * @param studentId 学生ID
     * @return 分页后的反馈列表
     */
    IPage<FeedbackRecords> getStudentFeedbackRecordsPage(Page<FeedbackRecords> page, Long studentId);
    
    /**
     * 添加反馈
     * @param record 反馈记录
     * @return 是否添加成功
     */
    boolean addFeedbackRecord(FeedbackRecords record);
    
    /**
     * 更新反馈
     * @param record 反馈记录
     * @return 是否更新成功
     */
    boolean updateFeedbackRecord(FeedbackRecords record);
    
    /**
     * 删除反馈
     * @param feedbackId 反馈ID
     * @param studentId 学生ID（用于验证权限）
     * @return 是否删除成功
     */
    boolean deleteFeedbackRecord(Long feedbackId, Long studentId);
    
    /**
     * 根据ID获取反馈
     * @param feedbackId 反馈ID
     * @return 反馈信息
     */
    FeedbackRecords getFeedbackRecordById(Long feedbackId);
}
