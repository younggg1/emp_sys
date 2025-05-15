package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.School;
import com.empsys.backend.entity.SystemSettings;
import com.empsys.backend.entity.User;
import com.empsys.backend.mapper.SchoolMapper;
import com.empsys.backend.mapper.SystemSettingsMapper;
import com.empsys.backend.mapper.UserMapper;
import com.empsys.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private SchoolMapper schoolMapper;
    
    @Autowired
    private SystemSettingsMapper systemSettingsMapper;
    
    @Override
    public Response<Map<String, Object>> login(User loginUser) {
        // 验证学校是否存在
        School school = schoolMapper.selectById(loginUser.getSchoolId());
        if (school == null) {
            return Response.error("学校不存在");
        }
        
        // 通过用户名和密码查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getSchoolId, loginUser.getSchoolId())
                .eq(User::getUsername, loginUser.getUsername())
                .eq(User::getPassword, loginUser.getPassword());
        
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Response.error("用户名或密码错误");
        }
        
        // 返回登录信息
        Map<String, Object> result = new HashMap<>();
        result.put("user_id", user.getUserId());
        result.put("role", user.getRole());
        result.put("school_id", user.getSchoolId());
        
        return Response.success("登录成功", result);
    }
    
    @Override
    public Response<List<School>> getSchools() {
        List<School> schools = schoolMapper.selectList(null);
        return Response.success(schools);
    }
    
    @Override
    public Response<SystemSettings> getSettings(Long schoolId) {
        // 验证学校是否存在
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            return Response.error("学校不存在");
        }
        
        // 查询学校系统设置
        LambdaQueryWrapper<SystemSettings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemSettings::getSchoolId, schoolId);
        
        SystemSettings settings = systemSettingsMapper.selectOne(queryWrapper);
        if (settings == null) {
            // 如果没有设置，返回默认设置
            settings = new SystemSettings();
            settings.setSchoolId(schoolId);
            settings.setRequireCaptcha(true);
            settings.setRequireApproval(true);
        }
        
        return Response.success(settings);
    }
} 
