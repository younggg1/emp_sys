package com.empsys.backend.service;

import com.empsys.backend.entity.SystemSettings;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @description 针对表【system_settings】的数据库操作Service
*/
public interface SystemSettingsService extends IService<SystemSettings> {
    
    /**
     * 获取系统设置
     * @return 系统设置
     */
    SystemSettings getSystemSettings();
    
    /**
     * 更新验证码设置
     * @param requireCaptcha 是否需要验证码
     * @return 更新后的系统设置
     */
    SystemSettings updateCaptchaSetting(Boolean requireCaptcha);
} 