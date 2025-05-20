package com.empsys.backend.service.impl;

import com.empsys.backend.mapper.StatisticsMapper;
import com.empsys.backend.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 就业数据统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public Map<String, Object> getBasicStats(String year) {
        try {
            logger.info("开始获取基础统计数据，年份：{}", year);
            Map<String, Object> stats = statisticsMapper.getBasicStats(year);
            
            if (stats == null) {
                logger.error("获取基础统计数据为空");
                throw new RuntimeException("获取基础统计数据为空");
            }
            
            // 计算就业率
            int totalStudents = ((Number) stats.get("total_students")).intValue();
            int totalEmployed = ((Number) stats.get("total_employed")).intValue();
            double employmentRate = totalStudents > 0 ? 
                (double) totalEmployed / totalStudents * 100 : 0;
            
            stats.put("employment_rate", Math.round(employmentRate * 100) / 100.0);
            
            logger.info("基础统计数据计算完成：{}", stats);
            return stats;
        } catch (Exception e) {
            logger.error("获取基础统计数据失败", e);
            throw new RuntimeException("获取基础统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getCompanyNatureStats(String year) {
        return statisticsMapper.getCompanyNatureStats(year);
    }

    @Override
    public List<Map<String, Object>> getSalaryStats(String year) {
        return statisticsMapper.getSalaryStats(year);
    }

    @Override
    public List<Map<String, Object>> getRegionStats(String year) {
        return statisticsMapper.getRegionStats(year);
    }
} 