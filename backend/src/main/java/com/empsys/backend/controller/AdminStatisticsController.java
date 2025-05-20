package com.empsys.backend.controller;

import com.empsys.backend.service.StatisticsService;
import com.empsys.backend.config.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AdminStatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取基础统计数据
     * @param year 年份
     * @return 基础统计数据
     */
    @GetMapping("/basic")
    public Result<Map<String, Object>> getBasicStats(@RequestParam(required = false) String year) {
        try {
            logger.info("获取基础统计数据，年份：{}", year);
            Map<String, Object> data = statisticsService.getBasicStats(year);
            logger.info("获取基础统计数据成功：{}", data);
            return Result.success(data);
        } catch (Exception e) {
            logger.error("获取基础统计数据失败", e);
            return Result.error("获取基础统计数据失败：" + e.getMessage());
        }
    }

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