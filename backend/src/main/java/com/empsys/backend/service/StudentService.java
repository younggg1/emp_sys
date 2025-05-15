package com.empsys.backend.service;

import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.EmploymentRecord;
import com.empsys.backend.entity.FeedbackRecord;
import com.empsys.backend.entity.Student;

import java.util.List;
import java.util.Map;

public interface StudentService {
    
    /**
     * 获取学生信息
     * @param studentId 学生ID
     * @return 学生信息
     */
    Response<Student> getStudentInfo(Long studentId);
    
    /**
     * 登记就业信息
     * @param studentId 学生ID
     * @param employmentRecord 就业信息
     * @return 登记结果
     */
    Response<Map<String, Object>> registerEmployment(Long studentId, EmploymentRecord employmentRecord);
    
    /**
     * 更新就业信息
     * @param studentId 学生ID
     * @param recordId 记录ID
     * @param employmentRecord 就业信息
     * @return 更新结果
     */
    Response<Map<String, Object>> updateEmployment(Long studentId, Long recordId, EmploymentRecord employmentRecord);
    
    /**
     * 删除就业信息
     * @param studentId 学生ID
     * @param recordId 记录ID
     * @return 删除结果
     */
    Response<Void> deleteEmployment(Long studentId, Long recordId);
    
    /**
     * 获取学生就业信息
     * @param studentId 学生ID
     * @return 就业信息列表
     */
    Response<List<EmploymentRecord>> getEmploymentRecords(Long studentId);
    
    /**
     * 提交就业反馈
     * @param studentId 学生ID
     * @param feedbackRecord 反馈信息
     * @return 提交结果
     */
    Response<Map<String, Object>> submitFeedback(Long studentId, FeedbackRecord feedbackRecord);
    
    /**
     * 更新就业反馈
     * @param studentId 学生ID
     * @param feedbackId 反馈ID
     * @param feedbackRecord 反馈信息
     * @return 更新结果
     */
    Response<Map<String, Object>> updateFeedback(Long studentId, Long feedbackId, FeedbackRecord feedbackRecord);
    
    /**
     * 删除就业反馈
     * @param studentId 学生ID
     * @param feedbackId 反馈ID
     * @return 删除结果
     */
    Response<Void> deleteFeedback(Long studentId, Long feedbackId);
    
    /**
     * 获取学生反馈信息
     * @param studentId 学生ID
     * @return 反馈信息列表
     */
    Response<List<FeedbackRecord>> getFeedbackRecords(Long studentId);
}
