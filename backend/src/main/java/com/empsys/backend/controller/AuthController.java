package com.empsys.backend.controller;

import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.School;
import com.empsys.backend.entity.SystemSettings;
import com.empsys.backend.entity.User;
import com.empsys.backend.mapper.SystemSettingsMapper;
import com.empsys.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private SystemSettingsMapper systemSettingsMapper;

    @GetMapping("/captcha")
    public Response<Boolean> getCaptchaSetting(@RequestParam Long schoolId) {
        String value = systemSettingsMapper.getValueBySchoolId(schoolId.intValue());
        // 明确返回布尔值
        boolean enabled = true; // 默认值
        if (value != null) {
            enabled = "1".equals(value);
        }
        return Response.success(enabled);
    }

    @PostMapping("/updateCaptcha")
    public ResponseEntity<?> updateCaptchaSetting(@RequestBody Map<String, Boolean> body) {
        try {
            boolean enabled = body.get("enabled");
            systemSettingsMapper.updateValue("captcha_enabled", String.valueOf(enabled));
            // 返回更新后的布尔值
            return ResponseEntity.ok(Map.of(
                    "message", "设置更新成功",
                    "value", enabled
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "更新失败：" + e.getMessage()));
        }
    }
}
