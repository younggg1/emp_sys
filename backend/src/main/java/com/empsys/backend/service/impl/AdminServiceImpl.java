package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.Counselor;
import com.empsys.backend.entity.EmploymentRecord;
import com.empsys.backend.entity.FeedbackRecord;
import com.empsys.backend.entity.Permission;
import com.empsys.backend.entity.School;
import com.empsys.backend.entity.Student;
import com.empsys.backend.entity.SystemSettings;
import com.empsys.backend.entity.User;
import com.empsys.backend.mapper.CounselorMapper;
import com.empsys.backend.mapper.EmploymentMapper;
import com.empsys.backend.mapper.FeedbackMapper;
import com.empsys.backend.mapper.PermissionMapper;
import com.empsys.backend.mapper.SchoolMapper;
import com.empsys.backend.mapper.StudentMapper;
import com.empsys.backend.mapper.SystemSettingsMapper;
import com.empsys.backend.mapper.UserMapper;
import com.empsys.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private EmploymentMapper employmentMapper;
    
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CounselorMapper counselorMapper;
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private SystemSettingsMapper systemSettingsMapper;
    
    @Autowired
    private SchoolMapper schoolMapper;
    
    @Override
    public Response<IPage<Student>> getStudents(Long schoolId, long current, long size) {
        // 验证学校是否存在
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            return Response.error("学校不存在");
        }
        
        // 获取学校的学生列表
        Page<Student> page = new Page<>(current, size);
        IPage<Student> students = studentMapper.getStudentsBySchool(page, schoolId);
        return Response.success(students);
    }
    
    @Override
    public Response<IPage<EmploymentRecord>> getEmploymentRecords(Long schoolId, Integer year, long current, long size) {
        // 验证学校是否存在
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            return Response.error("学校不存在");
        }
        
        // 获取学校的就业记录
        Page<EmploymentRecord> page = new Page<>(current, size);
        IPage<EmploymentRecord> records = employmentMapper.getRecordsBySchool(page, schoolId, year);
        return Response.success(records);
    }
    
    @Override
    public Response<IPage<FeedbackRecord>> getFeedbackRecords(Long schoolId, Integer year, long current, long size) {
        // 验证学校是否存在
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            return Response.error("学校不存在");
        }
        
        // 获取学校的反馈记录
        Page<FeedbackRecord> page = new Page<>(current, size);
        IPage<FeedbackRecord> records = feedbackMapper.getFeedbackBySchool(page, schoolId, year);
        return Response.success(records);
    }
    
    @Override
    @Transactional
    public Response<Map<String, Object>> addUser(Long schoolId, Map<String, Object> userInfo) {
        // 验证学校是否存在
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            return Response.error("学校不存在");
        }
        
        // 获取用户信息
        String username = (String) userInfo.get("username");
        String password = (String) userInfo.get("password");
        String role = (String) userInfo.get("role");
        String name = (String) userInfo.get("name");
        
        // 验证用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getSchoolId, schoolId)
                .eq(User::getUsername, username);
        if (userMapper.selectCount(queryWrapper) > 0) {
            return Response.error("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUserId(System.currentTimeMillis());
        user.setSchoolId(schoolId);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        userMapper.insert(user);
        
        // 根据角色创建对应信息
        if ("student".equals(role)) {
            Student student = new Student();
            student.setStudentId(user.getUserId());
            student.setSchoolId(schoolId);
            student.setName(name);
            student.setClassName((String) userInfo.get("class_name"));
            student.setCollege((String) userInfo.get("college"));
            student.setMajor((String) userInfo.get("major"));
            student.setCounselorId((Long) userInfo.get("counselor_id"));
            student.setEmploymentStatus("unemployed");
            studentMapper.insert(student);
        } else if ("counselor".equals(role)) {
            Counselor counselor = new Counselor();
            counselor.setCounselorId(user.getUserId());
            counselor.setSchoolId(schoolId);
            counselor.setName(name);
            counselorMapper.insert(counselor);
            
            // 创建默认权限（无编辑和删除权限）
            Permission permission = new Permission();
            permission.setPermissionId(System.currentTimeMillis());
            permission.setUserId(user.getUserId());
            permission.setCanEdit(false);
            permission.setCanDelete(false);
            permissionMapper.insert(permission);
        }
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("user_id", user.getUserId());
        
        return Response.success("添加成功", result);
    }
    
    @Override
    public Response<Void> resetPassword(Long schoolId, Long userId, String newPassword) {
        // 验证学校和用户是否匹配
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getSchoolId, schoolId)
                .eq(User::getUserId, userId);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Response.error("用户不存在或不属于该学校");
        }
        
        // 更新密码
        user.setPassword(newPassword);
        userMapper.updateById(user);
        
        return Response.success("重置成功");
    }
    
    @Override
    @Transactional
    public Response<Void> deleteUser(Long schoolId, Long userId) {
        // 验证学校和用户是否匹配
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getSchoolId, schoolId)
                .eq(User::getUserId, userId);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Response.error("用户不存在或不属于该学校");
        }
        
        // 验证用户不是管理员
        if ("admin".equals(user.getRole())) {
            return Response.error("不能删除管理员账号");
        }
        
        // 删除用户（级联删除其他表的记录）
        userMapper.deleteById(userId);
        
        return Response.success("删除成功");
    }
    
    @Override
    public Response<Void> assignPermission(Long schoolId, Long userId, Map<String, Boolean> permissions) {
        // 验证学校和用户是否匹配，且用户为辅导员
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getSchoolId, schoolId)
                .eq(User::getUserId, userId)
                .eq(User::getRole, "counselor");
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Response.error("用户不存在或不是辅导员");
        }
        
        // 查询权限记录
        Permission permission = permissionMapper.getPermissionByUserId(userId);
        if (permission == null) {
            // 创建新权限记录
            permission = new Permission();
            permission.setPermissionId(System.currentTimeMillis());
            permission.setUserId(userId);
            permission.setCanEdit(permissions.getOrDefault("can_edit", false));
            permission.setCanDelete(permissions.getOrDefault("can_delete", false));
            permissionMapper.insert(permission);
        } else {
            // 更新权限记录
            permission.setCanEdit(permissions.getOrDefault("can_edit", false));
            permission.setCanDelete(permissions.getOrDefault("can_delete", false));
            permissionMapper.updateById(permission);
        }
        
        return Response.success("分配成功");
    }
    
    @Override
    public Response<Void> updateSettings(Long schoolId, SystemSettings settings) {
        // 验证学校是否存在
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            return Response.error("学校不存在");
        }
        
        // 查询系统设置
        LambdaQueryWrapper<SystemSettings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemSettings::getSchoolId, schoolId);
        SystemSettings existingSettings = systemSettingsMapper.selectOne(queryWrapper);
        
        if (existingSettings == null) {
            // 创建新设置
            settings.setSettingId(System.currentTimeMillis());
            settings.setSchoolId(schoolId);
            systemSettingsMapper.insert(settings);
        } else {
            // 更新设置
            existingSettings.setRequireCaptcha(settings.getRequireCaptcha());
            existingSettings.setRequireApproval(settings.getRequireApproval());
            systemSettingsMapper.updateById(existingSettings);
        }
        
        return Response.success("更新成功");
    }
    
    @Override
    public Response<List<Map<String, Object>>> getDistribution(Long schoolId, String type, Integer year) {
        // 验证学校是否存在
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            return Response.error("学校不存在");
        }
        
        // 获取指定类型的分布统计
        List<Map<String, Object>> statistics;
        switch (type) {
            case "region":
                statistics = employmentMapper.getRegionDistribution(null, schoolId, year);
                break;
            case "salary":
                statistics = employmentMapper.getSalaryDistribution(null, schoolId, year);
                break;
            case "company_nature":
                statistics = employmentMapper.getCompanyNatureDistribution(null, schoolId, year);
                break;
            default:
                return Response.error("统计类型错误");
        }
        
        return Response.success(statistics);
    }
    
    @Override
    public Response<List<Map<String, Object>>> getTrend(Long schoolId, Integer startYear, Integer endYear) {
        // 验证学校是否存在
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            return Response.error("学校不存在");
        }
        
        // 获取学校的就业趋势
        List<Map<String, Object>> trend = employmentMapper.getEmploymentTrend(null, schoolId, startYear, endYear);
        return Response.success(trend);
    }
} 
