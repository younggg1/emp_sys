package com.empsys.backend.controller;

import com.empsys.backend.config.Result;
import com.empsys.backend.entity.SystemSettings;
import com.empsys.backend.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统设置控制器
 */
@RestController
@RequestMapping("/api/settings")
public class SystemSettingsController {

    @Autowired
    private SystemSettingsService systemSettingsService;

    /**
     * 获取系统设置
     * @return 系统设置
     */
    @GetMapping("/get")
    public Result<Map<String, Object>> getSettings() {
        SystemSettings settings = systemSettingsService.getSystemSettings();
        
        if (settings == null) {
            return Result.error("获取系统设置失败");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("requireCaptcha", settings.getRequireCaptcha());
        
        // 不再返回requireApproval参数
        // data.put("requireApproval", settings.getRequireApproval());
        
        return Result.success(data);
    }

    /**
     * 更新验证码设置
     * @param params 包含验证码开关状态的Map
     * @return 更新结果
     */
    @PostMapping("/captcha")
    public Result<Boolean> updateCaptchaSetting(@RequestBody Map<String, Boolean> params) {
        Boolean requireCaptcha = params.get("requireCaptcha");
        
        if (requireCaptcha == null) {
            return Result.error("参数错误");
        }
        
        SystemSettings settings = systemSettingsService.updateCaptchaSetting(requireCaptcha);
        
        if (settings == null) {
            return Result.error("更新验证码设置失败");
        }
        
        return Result.success(true);
    }
} 