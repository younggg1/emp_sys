package com.empsys.backend.service;

import com.empsys.backend.entity.EmploymentRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
* @author Quagmire
* @description 针对表【employment_records】的数据库操作Service
* @createDate 2025-05-16 17:49:01
*/
public interface EmploymentRecordsService extends IService<EmploymentRecords> {
    
    /**
     * 获取学生的就业信息列表
     * @param studentId 学生ID
     * @return 就业信息列表
     */
    List<EmploymentRecords> getStudentEmploymentRecords(Long studentId);
    
    /**
     * 分页获取学生的就业信息列表
     * @param page 分页参数
     * @param studentId 学生ID
     * @return 分页后的就业信息列表
     */
    IPage<EmploymentRecords> getStudentEmploymentRecordsPage(Page<EmploymentRecords> page, Long studentId);
    
    /**
     * 添加就业信息
     * @param record 就业信息记录
     * @return 是否添加成功
     */
    boolean addEmploymentRecord(EmploymentRecords record);
    
    /**
     * 更新就业信息
     * @param record 就业信息记录
     * @return 是否更新成功
     */
    boolean updateEmploymentRecord(EmploymentRecords record);
    
    /**
     * 删除就业信息
     * @param recordId 记录ID
     * @param studentId 学生ID（用于验证权限）
     * @return 是否删除成功
     */
    boolean deleteEmploymentRecord(Long recordId, Long studentId);
    
    /**
     * 根据ID获取就业信息
     * @param recordId 记录ID
     * @return 就业信息
     */
    EmploymentRecords getEmploymentRecordById(Long recordId);
    
    /**
     * 获取辅导员负责的学生就业信息列表
     * @param counselorId 辅导员ID
     * @return 就业信息列表（包含学生信息）
     */
    List<Map<String, Object>> getEmploymentRecordsByCounselorId(Long counselorId);
    
    /**
     * 辅导员审核就业信息
     * @param recordId 记录ID
     * @param status 状态（approved/rejected）
     * @param counselorId 辅导员ID（用于验证权限）
     * @return 是否审核成功
     */
    boolean approveEmploymentRecord(Long recordId, String status, Long counselorId);
    

    
    /**
     * 辅导员删除就业信息
     * @param recordId 记录ID
     * @param counselorId 辅导员ID（用于验证权限）
     * @return 是否删除成功
     */
    boolean deleteEmploymentRecordByCounselor(Long recordId, Long counselorId);
}
