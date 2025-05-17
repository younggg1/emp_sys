package com.empsys.backend.controller;

import com.empsys.backend.service.StatisticsService;
import com.empsys.backend.config.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员统计控制器
 */
@RestController
@RequestMapping("/api/admin/statistics")
public class AdminStatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取企业性质分布统计
     * @param year 年份（可选）
     * @return 企业性质分布数据
     */
    @GetMapping("/company-nature")
    public Result<List<Map<String, Object>>> getCompanyNatureStats(@RequestParam(required = false) String year) {
        try {
            List<Map<String, Object>> data = statisticsService.getCompanyNatureStats(year);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取企业性质分布统计失败");
        }
    }

    /**
     * 获取薪资分布统计
     * @param year 年份（可选）
     * @return 薪资分布数据
     */
    @GetMapping("/salary")
    public Result<List<Map<String, Object>>> getSalaryStats(@RequestParam(required = false) String year) {
        try {
            List<Map<String, Object>> data = statisticsService.getSalaryStats(year);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取薪资分布统计失败");
        }
    }

    /**
     * 获取地区分布统计
     * @param year 年份（可选）
     * @return 地区分布数据
     */
    @GetMapping("/region")
    public Result<List<Map<String, Object>>> getRegionStats(@RequestParam(required = false) String year) {
        try {
            List<Map<String, Object>> data = statisticsService.getRegionStats(year);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取地区分布统计失败");
        }
    }
} 