package com.empsys.backend.service;

import com.empsys.backend.entity.FeedbackRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

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
    
    /**
     * 获取辅导员负责的学生反馈列表
     * @param counselorId 辅导员ID
     * @return 反馈列表（包含学生信息）
     */
    List<Map<String, Object>> getFeedbackRecordsByCounselorId(Long counselorId);
    
    /**
     * 分页获取辅导员负责的学生反馈列表
     * @param page 分页参数
     * @param counselorId 辅导员ID
     * @return 分页后的反馈列表（包含学生信息）
     */
    IPage<Map<String, Object>> getFeedbackRecordsPageByCounselorId(Page<Map<String, Object>> page, Long counselorId);
    
    /**
     * 辅导员审核反馈
     * @param feedbackId 反馈ID
     * @param status 审核状态
     * @param counselorId 辅导员ID（用于验证权限）
     * @return 是否审核成功
     */
    boolean approveFeedbackRecord(Long feedbackId, String status, Long counselorId);
    
    /**
     * 辅导员删除反馈
     * @param feedbackId 反馈ID
     * @param counselorId 辅导员ID（用于验证权限）
     * @return 是否删除成功
     */
    boolean deleteFeedbackRecordByCounselor(Long feedbackId, Long counselorId);
    
    /**
     * 获取反馈详情（辅导员视角）
     * @param feedbackId 反馈ID
     * @param counselorId 辅导员ID（用于验证权限）
     * @return 反馈详情（包含学生信息）
     */
    Map<String, Object> getFeedbackRecordDetailByCounselor(Long feedbackId, Long counselorId);
}
