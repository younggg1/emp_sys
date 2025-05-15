package com.empsys.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.EmploymentRecord;
import com.empsys.backend.entity.FeedbackRecord;
import com.empsys.backend.entity.Student;

import java.util.List;
import java.util.Map;

public interface CounselorService {
    
    /**
     * 获取辅导员管辖的学生列表
     * @param counselorId 辅导员ID
     * @return 学生列表
     */
    Response<List<Student>> getStudents(Long counselorId);
    
    /**
     * 获取辅导员管辖的学生就业信息
     * @param counselorId 辅导员ID
     * @param current 当前页
     * @param size 每页数量
     * @return 就业信息分页数据
     */
    Response<IPage<EmploymentRecord>> getEmploymentRecords(Long counselorId, long current, long size);
    
    /**
     * 获取辅导员管辖的学生反馈信息
     * @param counselorId 辅导员ID
     * @param current 当前页
     * @param size 每页数量
     * @return 反馈信息分页数据
     */
    Response<IPage<FeedbackRecord>> getFeedbackRecords(Long counselorId, long current, long size);
    
    /**
     * 审核就业信息
     * @param counselorId 辅导员ID
     * @param recordId 记录ID
     * @return 审核结果
     */
    Response<Void> auditEmployment(Long counselorId, Long recordId);
    
    /**
     * 审核反馈信息
     * @param counselorId 辅导员ID
     * @param feedbackId 反馈ID
     * @return 审核结果
     */
    Response<Void> auditFeedback(Long counselorId, Long feedbackId);
    
    /**
     * 编辑就业信息（需要权限）
     * @param counselorId 辅导员ID
     * @param recordId 记录ID
     * @param employmentRecord 就业信息
     * @return 编辑结果
     */
    Response<Void> editEmployment(Long counselorId, Long recordId, EmploymentRecord employmentRecord);
    
    /**
     * 删除就业信息（需要权限）
     * @param counselorId 辅导员ID
     * @param recordId 记录ID
     * @return 删除结果
     */
    Response<Void> deleteEmployment(Long counselorId, Long recordId);
    
    /**
     * 删除反馈信息（需要权限）
     * @param counselorId 辅导员ID
     * @param feedbackId 反馈ID
     * @return 删除结果
     */
    Response<Void> deleteFeedback(Long counselorId, Long feedbackId);
    
    /**
     * 获取辅导员管辖班级的历年就业信息
     * @param counselorId 辅导员ID
     * @param year 年份（可选）
     * @param current 当前页
     * @param size 每页数量
     * @return 就业信息分页数据
     */
    Response<IPage<EmploymentRecord>> getHistoryEmployment(Long counselorId, Integer year, long current, long size);
    
    /**
     * 统计辅导员管辖班级的就业分布（地区、薪资、企业性质）
     * @param counselorId 辅导员ID
     * @param type 统计类型（region/salary/company_nature）
     * @param year 年份（可选）
     * @return 分布统计数据
     */
    Response<List<Map<String, Object>>> getDistribution(Long counselorId, String type, Integer year);
    
    /**
     * 统计辅导员管辖班级的就业趋势
     * @param counselorId 辅导员ID
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @return 趋势统计数据
     */
    Response<List<Map<String, Object>>> getTrend(Long counselorId, Integer startYear, Integer endYear);
}
