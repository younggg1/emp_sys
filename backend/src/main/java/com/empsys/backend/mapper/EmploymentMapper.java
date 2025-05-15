package com.empsys.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.empsys.backend.entity.EmploymentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmploymentMapper extends BaseMapper<EmploymentRecord> {
    
    /**
     * 获取指定学生的就业记录
     * @param studentId 学生ID
     * @return 就业记录列表
     */
    List<EmploymentRecord> getRecordsByStudent(@Param("studentId") Long studentId);
    
    /**
     * 获取指定辅导员管理的学生的就业记录
     * @param page 分页参数
     * @param counselorId 辅导员ID
     * @return 就业记录分页数据
     */
    IPage<EmploymentRecord> getRecordsByCounselor(Page<EmploymentRecord> page, @Param("counselorId") Long counselorId);
    
    /**
     * 获取指定学校的就业记录
     * @param page 分页参数
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @return 就业记录分页数据
     */
    IPage<EmploymentRecord> getRecordsBySchool(Page<EmploymentRecord> page, @Param("schoolId") Long schoolId, @Param("year") Integer year);
    
    /**
     * 统计就业地区分布
     * @param counselorId 辅导员ID（可选）
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @return 地区分布统计数据
     */
    List<Map<String, Object>> getRegionDistribution(@Param("counselorId") Long counselorId, @Param("schoolId") Long schoolId, @Param("year") Integer year);
    
    /**
     * 统计就业薪资分布
     * @param counselorId 辅导员ID（可选）
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @return 薪资分布统计数据
     */
    List<Map<String, Object>> getSalaryDistribution(@Param("counselorId") Long counselorId, @Param("schoolId") Long schoolId, @Param("year") Integer year);
    
    /**
     * 统计企业性质分布
     * @param counselorId 辅导员ID（可选）
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @return 企业性质分布统计数据
     */
    List<Map<String, Object>> getCompanyNatureDistribution(@Param("counselorId") Long counselorId, @Param("schoolId") Long schoolId, @Param("year") Integer year);
    
    /**
     * 统计就业趋势
     * @param counselorId 辅导员ID（可选）
     * @param schoolId 学校ID
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @return 就业趋势统计数据
     */
    List<Map<String, Object>> getEmploymentTrend(@Param("counselorId") Long counselorId, @Param("schoolId") Long schoolId, 
                                          @Param("startYear") Integer startYear, @Param("endYear") Integer endYear);
} 
