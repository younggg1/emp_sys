package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.empsys.backend.entity.SystemSettings;
import com.empsys.backend.service.SystemSettingsService;
import com.empsys.backend.mapper.SystemSettingsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @description 针对表【system_settings】的数据库操作Service实现
*/
@Service
public class SystemSettingsServiceImpl extends ServiceImpl<SystemSettingsMapper, SystemSettings>
    implements SystemSettingsService{

    @Autowired
    private SystemSettingsMapper systemSettingsMapper;
    
    @Override
    public SystemSettings getSystemSettings() {
        return systemSettingsMapper.getSystemSettings();
    }
    
    @Override
    public SystemSettings updateCaptchaSetting(Boolean requireCaptcha) {
        SystemSettings settings = getSystemSettings();
        if (settings != null) {
            settings.setRequireCaptcha(requireCaptcha);
            this.updateById(settings);
            return settings;
        }
        return null;
    }
} 