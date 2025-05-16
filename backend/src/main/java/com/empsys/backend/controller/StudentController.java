package com.empsys.backend.controller;

import com.empsys.backend.config.Result;
import com.empsys.backend.entity.Students;
import com.empsys.backend.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 学生相关接口控制器
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentsService studentsService;

    /**
     * 获取学生个人信息
     * @param userId 用户ID
     * @return 学生个人信息
     */
    @GetMapping("/info")
    public Result<Students> getStudentInfo(@RequestParam Long userId) {
        // 调用服务获取学生信息
        Students studentInfo = studentsService.getStudentInfo(userId);
        
        if (studentInfo == null) {
            return Result.error("获取学生信息失败，学生不存在");
        }
        
        return Result.success(studentInfo);
    }
} 