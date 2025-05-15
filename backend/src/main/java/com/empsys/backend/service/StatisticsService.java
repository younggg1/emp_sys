package com.empsys.backend.service;

import com.empsys.backend.entity.Response;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    
    /**
     * 获取地区分布统计
     * @param counselorId 辅导员ID（可选）
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @return 地区分布统计数据
     */
    List<Map<String, Object>> getRegionDistribution(Long counselorId, Long schoolId, Integer year);
    
    /**
     * 获取薪资分布统计
     * @param counselorId 辅导员ID（可选）
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @return 薪资分布统计数据
     */
    List<Map<String, Object>> getSalaryDistribution(Long counselorId, Long schoolId, Integer year);
    
    /**
     * 获取企业性质分布统计
     * @param counselorId 辅导员ID（可选）
     * @param schoolId 学校ID
     * @param year 年份（可选）
     * @return 企业性质分布统计数据
     */
    List<Map<String, Object>> getCompanyNatureDistribution(Long counselorId, Long schoolId, Integer year);
    
    /**
     * 获取就业趋势统计
     * @param counselorId 辅导员ID（可选）
     * @param schoolId 学校ID
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @return 就业趋势统计数据
     */
    List<Map<String, Object>> getEmploymentTrend(Long counselorId, Long schoolId, Integer startYear, Integer endYear);
}
