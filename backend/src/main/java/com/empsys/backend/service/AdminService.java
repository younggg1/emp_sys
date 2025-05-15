package com.empsys.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.EmploymentRecord;
import com.empsys.backend.entity.FeedbackRecord;
import com.empsys.backend.entity.Student;
import com.empsys.backend.entity.SystemSettings;

import java.util.List;
import java.util.Map;

public interface AdminService {
    
    /**
     * 获取学校的所有学生
     * @param schoolId 学校ID
     * @param current 当前页
     * @param size 每页数量
     * @return 学生分页数据
     */
    Response<IPage<Student>> getStudents(Long schoolId, long current, long size);
    
    /**
     * 获取学校的所有就业信息
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @param current 当前页
     * @param size 每页数量
     * @return 就业信息分页数据
     */
    Response<IPage<EmploymentRecord>> getEmploymentRecords(Long schoolId, Integer year, long current, long size);
    
    /**
     * 获取学校的所有反馈信息
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @param current 当前页
     * @param size 每页数量
     * @return 反馈信息分页数据
     */
    Response<IPage<FeedbackRecord>> getFeedbackRecords(Long schoolId, Integer year, long current, long size);
    
    /**
     * 添加用户（学生或辅导员）
     * @param schoolId 学校ID
     * @param userInfo 用户信息
     * @return 添加结果
     */
    Response<Map<String, Object>> addUser(Long schoolId, Map<String, Object> userInfo);
    
    /**
     * 重置用户密码
     * @param schoolId 学校ID
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 重置结果
     */
    Response<Void> resetPassword(Long schoolId, Long userId, String newPassword);
    
    /**
     * 删除用户
     * @param schoolId 学校ID
     * @param userId 用户ID
     * @return 删除结果
     */
    Response<Void> deleteUser(Long schoolId, Long userId);
    
    /**
     * 分配权限
     * @param schoolId 学校ID
     * @param userId 用户ID
     * @param permissions 权限信息
     * @return 分配结果
     */
    Response<Void> assignPermission(Long schoolId, Long userId, Map<String, Boolean> permissions);
    
    /**
     * 更新系统设置
     * @param schoolId 学校ID
     * @param settings 系统设置
     * @return 更新结果
     */
    Response<Void> updateSettings(Long schoolId, SystemSettings settings);
    
    /**
     * 统计学校的就业分布（地区、薪资、企业性质）
     * @param schoolId 学校ID
     * @param type 统计类型（region/salary/company_nature）
     * @param year 年份（可选）
     * @return 分布统计数据
     */
    Response<List<Map<String, Object>>> getDistribution(Long schoolId, String type, Integer year);
    
    /**
     * 统计学校的就业趋势
     * @param schoolId 学校ID
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @return 趋势统计数据
     */
    Response<List<Map<String, Object>>> getTrend(Long schoolId, Integer startYear, Integer endYear);
} 
