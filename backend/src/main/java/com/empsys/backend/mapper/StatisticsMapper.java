package com.empsys.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 统计相关的Mapper接口
 */
@Mapper
public interface StatisticsMapper {
    
    /**
     * 获取企业性质分布统计
     * @param year 年份（可选）
     * @return 企业性质分布数据
     */
    List<Map<String, Object>> getCompanyNatureStats(@Param("year") String year);
    
    /**
     * 获取薪资分布统计
     * @param year 年份（可选）
     * @return 薪资分布数据
     */
    List<Map<String, Object>> getSalaryStats(@Param("year") String year);
    
    /**
     * 获取地区分布统计
     * @param year 年份（可选）
     * @return 地区分布数据
     */
    List<Map<String, Object>> getRegionStats(@Param("year") String year);
} 