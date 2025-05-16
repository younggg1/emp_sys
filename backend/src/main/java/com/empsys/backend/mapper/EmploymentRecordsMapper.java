package com.empsys.backend.mapper;

import com.empsys.backend.entity.EmploymentRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Quagmire
* @description 针对表【employment_records】的数据库操作Mapper
* @createDate 2025-05-16 17:49:01
* @Entity com.empsys.backend.entity.EmploymentRecords
*/
@Mapper
public interface EmploymentRecordsMapper extends BaseMapper<EmploymentRecords> {

    /**
     * 获取学生的就业信息列表
     * @param studentId 学生ID
     * @return 就业信息列表
     */
    List<EmploymentRecords> selectEmploymentRecordsByStudentId(@Param("studentId") Long studentId);
    
    /**
     * 分页获取学生的就业信息列表
     * @param page 分页参数
     * @param studentId 学生ID
     * @return 分页后的就业信息列表
     */
    IPage<EmploymentRecords> selectEmploymentRecordsPageByStudentId(Page<EmploymentRecords> page, @Param("studentId") Long studentId);
    
    /**
     * 添加就业信息
     * @param record 就业信息记录
     * @return 影响行数
     */
    int insertEmploymentRecord(EmploymentRecords record);
    
    /**
     * 更新就业信息
     * @param record 就业信息记录
     * @return 影响行数
     */
    int updateEmploymentRecord(EmploymentRecords record);
    
    /**
     * 删除就业信息
     * @param recordId 记录ID
     * @return 影响行数
     */
    int deleteEmploymentRecord(@Param("recordId") Long recordId);
    
    /**
     * 根据ID获取就业信息
     * @param recordId 记录ID
     * @return 就业信息
     */
    EmploymentRecords selectEmploymentRecordById(@Param("recordId") Long recordId);
    
    /**
     * 验证就业信息是否属于指定学生
     * @param recordId 记录ID
     * @param studentId 学生ID
     * @return 就业信息记录数（应为0或1）
     */
    int countEmploymentRecordByIdAndStudentId(@Param("recordId") Long recordId, @Param("studentId") Long studentId);
    
    /**
     * 获取辅导员负责的学生就业信息列表
     * @param counselorId 辅导员ID
     * @return 就业信息列表（包含学生信息）
     */
    List<Map<String, Object>> selectEmploymentRecordsByCounselorId(@Param("counselorId") Long counselorId);
    
    /**
     * 验证就业信息是否属于辅导员负责的学生
     * @param recordId 记录ID
     * @param counselorId 辅导员ID
     * @return 就业信息记录数（应为0或1）
     */
    int countEmploymentRecordByIdAndCounselorId(@Param("recordId") Long recordId, @Param("counselorId") Long counselorId);
}




