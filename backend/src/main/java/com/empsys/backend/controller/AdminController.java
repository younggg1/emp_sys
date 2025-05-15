package com.empsys.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.EmploymentRecord;
import com.empsys.backend.entity.FeedbackRecord;
import com.empsys.backend.entity.Student;
import com.empsys.backend.entity.SystemSettings;
import com.empsys.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    /**
     * 获取学校学生列表
     */
    @GetMapping("/students")
    public Response<IPage<Student>> getStudents(
            @RequestParam Long schoolId,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        return adminService.getStudents(schoolId, current, size);
    }
    
    /**
     * 获取学校就业记录
     */
    @GetMapping("/employment")
    public Response<IPage<EmploymentRecord>> getEmploymentRecords(
            @RequestParam Long schoolId,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        return adminService.getEmploymentRecords(schoolId, year, current, size);
    }
    
    /**
     * 获取学校反馈记录
     */
    @GetMapping("/feedback")
    public Response<IPage<FeedbackRecord>> getFeedbackRecords(
            @RequestParam Long schoolId,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        return adminService.getFeedbackRecords(schoolId, year, current, size);
    }
    
    /**
     * 添加用户（学生或辅导员）
     */
    @PostMapping("/user/add")
    public Response<Map<String, Object>> addUser(
            @RequestParam Long schoolId,
            @RequestBody Map<String, Object> userInfo) {
        return adminService.addUser(schoolId, userInfo);
    }
    
    /**
     * 重置用户密码
     */
    @PutMapping("/user/reset-password")
    public Response<Void> resetPassword(
            @RequestParam Long schoolId,
            @RequestParam Long userId,
            @RequestParam String newPassword) {
        return adminService.resetPassword(schoolId, userId, newPassword);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/user/delete")
    public Response<Void> deleteUser(
            @RequestParam Long schoolId,
            @RequestParam Long userId) {
        return adminService.deleteUser(schoolId, userId);
    }
    
    /**
     * 分配权限
     */
    @PutMapping("/permission/assign")
    public Response<Void> assignPermission(
            @RequestParam Long schoolId,
            @RequestParam Long userId,
            @RequestBody Map<String, Boolean> permissions) {
        return adminService.assignPermission(schoolId, userId, permissions);
    }
    
    /**
     * 更新系统设置
     */
    @PutMapping("/settings/update")
    public Response<Void> updateSettings(
            @RequestParam Long schoolId,
            @RequestBody SystemSettings settings) {
        return adminService.updateSettings(schoolId, settings);
    }
    
    /**
     * 获取就业分布统计
     */
    @GetMapping("/statistics/distribution")
    public Response<List<Map<String, Object>>> getDistribution(
            @RequestParam Long schoolId,
            @RequestParam String type,
            @RequestParam(required = false) Integer year) {
        return adminService.getDistribution(schoolId, type, year);
    }
    
    /**
     * 获取就业趋势统计
     */
    @GetMapping("/statistics/trend")
    public Response<List<Map<String, Object>>> getTrend(
            @RequestParam Long schoolId,
            @RequestParam Integer startYear,
            @RequestParam Integer endYear) {
        return adminService.getTrend(schoolId, startYear, endYear);
    }
}
