package com.empsys.backend.controller;

import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.School;
import com.empsys.backend.entity.SystemSettings;
import com.empsys.backend.entity.User;
import com.empsys.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Response<Map<String, Object>> login(@RequestBody User user) {
        return authService.login(user);
    }
    
    /**
     * 获取学校列表
     */
    @GetMapping("/schools")
    public Response<List<School>> getSchools() {
        return authService.getSchools();
    }
    
    /**
     * 获取系统设置
     */
    @GetMapping("/settings")
    public Response<SystemSettings> getSettings(@RequestParam Long schoolId) {
        return authService.getSettings(schoolId);
    }
}
