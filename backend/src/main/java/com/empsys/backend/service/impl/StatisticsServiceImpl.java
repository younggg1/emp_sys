package com.empsys.backend.service.impl;

import com.empsys.backend.mapper.StatisticsMapper;
import com.empsys.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 就业数据统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public Map<String, Object> getBasicStats() {
        Map<String, Object> stats = statisticsMapper.getBasicStats();
        
        // 计算就业率
        int totalStudents = ((Number) stats.get("total_students")).intValue();
        int totalEmployed = ((Number) stats.get("total_employed")).intValue();
        double employmentRate = totalStudents > 0 ? 
            (double) totalEmployed / totalStudents * 100 : 0;
        
        stats.put("employment_rate", Math.round(employmentRate * 100) / 100.0);
        
        return stats;
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