package com.empsys.backend.service;

import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.School;
import com.empsys.backend.entity.SystemSettings;
import com.empsys.backend.entity.User;

import java.util.List;
import java.util.Map;

public interface AuthService {
    
    /**
     * 用户登录
     * @param user 登录信息
     * @return 登录结果
     */
    Response<Map<String, Object>> login(User user);
    
    /**
     * 获取学校列表
     * @return 学校列表
     */
    Response<List<School>> getSchools();
    
    /**
     * 获取系统设置
     * @param schoolId 学校ID
     * @return 系统设置
     */
    Response<SystemSettings> getSettings(Long schoolId);
} 
