package com.empsys.backend.service;

import java.util.List;
import java.util.Map;

/**
 * 就业数据统计服务接口
 */
public interface StatisticsService {
    
    /**
     * 获取基础统计数据
     * @return 基础统计数据
     */
    Map<String, Object> getBasicStats();
    
    /**
     * 获取企业性质分布统计
     * @param year 年份（可选）
     * @return 企业性质分布数据
     */
    List<Map<String, Object>> getCompanyNatureStats(String year);
    
    /**
     * 获取薪资分布统计
     * @param year 年份（可选）
     * @return 薪资分布数据
     */
    List<Map<String, Object>> getSalaryStats(String year);
    
    /**
     * 获取地区分布统计
     * @param year 年份（可选）
     * @return 地区分布数据
     */
    List<Map<String, Object>> getRegionStats(String year);
} 