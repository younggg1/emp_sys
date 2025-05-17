package com.empsys.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.config.Result;
import com.empsys.backend.entity.Users;
import com.empsys.backend.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/admin")
public class UserManagementController {

    private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    @Autowired
    private UserManagementService userManagementService;

    /**
     * 获取用户列表
     * @param page 页码
     * @param size 每页大小
     * @param role 角色筛选
     * @param keyword 关键词搜索
     * @return 用户列表
     */
    @GetMapping("/getUsersList")
    public Result<IPage<Users>> getUserList(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String keyword) {
        logger.info("获取用户列表，页码：{}，大小：{}，角色：{}，关键词：{}", page, size, role, keyword);
        Page<Users> pageParam = new Page<>(page, size);
        IPage<Users> users = userManagementService.getUserList(pageParam, role, keyword);
        return Result.success(users);
    }


    @PostMapping("/counselor")
    public Result<Boolean> addCounselor(@RequestBody Users user, @RequestParam String name) {
        try {
            boolean success = userManagementService.addCounselor(user, name);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取辅导员列表
     */
    @GetMapping("/getCounselors")
    public Result<List<Map<String, Object>>> getCounselorList() {
        logger.info("获取辅导员列表");
        List<Map<String, Object>> counselors = userManagementService.getCounselorList();
        return Result.success(counselors);
    }

    /**
     * 添加学生
     */
    @PostMapping("/addStudent")
    public Result<Boolean> addStudent(@RequestBody Map<String, Object> studentData) {
        try {
            boolean success = userManagementService.addStudent(studentData);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

} 