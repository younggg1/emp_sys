package com.empsys.backend.service.impl;

import com.empsys.backend.mapper.EmploymentMapper;
import com.empsys.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    
    @Autowired
    private EmploymentMapper employmentMapper;
    
    @Override
    public List<Map<String, Object>> getRegionDistribution(Long counselorId, Long schoolId, Integer year) {
        return employmentMapper.getRegionDistribution(counselorId, schoolId, year);
    }
    
    @Override
    public List<Map<String, Object>> getSalaryDistribution(Long counselorId, Long schoolId, Integer year) {
        return employmentMapper.getSalaryDistribution(counselorId, schoolId, year);
    }
    
    @Override
    public List<Map<String, Object>> getCompanyNatureDistribution(Long counselorId, Long schoolId, Integer year) {
        return employmentMapper.getCompanyNatureDistribution(counselorId, schoolId, year);
    }
    
    @Override
    public List<Map<String, Object>> getEmploymentTrend(Long counselorId, Long schoolId, Integer startYear, Integer endYear) {
        return employmentMapper.getEmploymentTrend(counselorId, schoolId, startYear, endYear);
    }
}
