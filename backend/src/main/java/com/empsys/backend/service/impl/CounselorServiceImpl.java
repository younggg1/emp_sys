package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.Counselor;
import com.empsys.backend.entity.EmploymentRecord;
import com.empsys.backend.entity.FeedbackRecord;
import com.empsys.backend.entity.Permission;
import com.empsys.backend.entity.Student;
import com.empsys.backend.mapper.CounselorMapper;
import com.empsys.backend.mapper.EmploymentMapper;
import com.empsys.backend.mapper.FeedbackMapper;
import com.empsys.backend.mapper.PermissionMapper;
import com.empsys.backend.mapper.StudentMapper;
import com.empsys.backend.service.CounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CounselorServiceImpl implements CounselorService {
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private EmploymentMapper employmentMapper;
    
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    @Autowired
    private CounselorMapper counselorMapper;
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Override
    public Response<List<Student>> getStudents(Long counselorId) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 获取辅导员管理的学生列表
        List<Student> students = studentMapper.getStudentsByCounselor(counselorId);
        return Response.success(students);
    }
    
    @Override
    public Response<IPage<EmploymentRecord>> getEmploymentRecords(Long counselorId, long current, long size) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 获取辅导员管理的学生就业信息
        Page<EmploymentRecord> page = new Page<>(current, size);
        IPage<EmploymentRecord> records = employmentMapper.getRecordsByCounselor(page, counselorId);
        return Response.success(records);
    }
    
    @Override
    public Response<IPage<FeedbackRecord>> getFeedbackRecords(Long counselorId, long current, long size) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 获取辅导员管理的学生反馈信息
        Page<FeedbackRecord> page = new Page<>(current, size);
        IPage<FeedbackRecord> records = feedbackMapper.getFeedbackByCounselor(page, counselorId);
        return Response.success(records);
    }
    
    @Override
    public Response<Void> auditEmployment(Long counselorId, Long recordId) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 查询就业记录
        EmploymentRecord record = employmentMapper.selectById(recordId);
        if (record == null) {
            return Response.error("就业记录不存在");
        }
        
        // 验证记录是否属于辅导员管理的学生
        boolean isManaged = isStudentManagedByCounselor(record.getStudentId(), counselorId);
        if (!isManaged) {
            return Response.error("无权限审核此记录");
        }
        
        // 审核记录
        record.setStatus("approved");
        employmentMapper.updateById(record);
        
        return Response.success("审核成功");
    }
    
    @Override
    public Response<Void> auditFeedback(Long counselorId, Long feedbackId) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 查询反馈记录
        FeedbackRecord record = feedbackMapper.selectById(feedbackId);
        if (record == null) {
            return Response.error("反馈记录不存在");
        }
        
        // 验证记录是否属于辅导员管理的学生
        boolean isManaged = isStudentManagedByCounselor(record.getStudentId(), counselorId);
        if (!isManaged) {
            return Response.error("无权限审核此记录");
        }
        
        // 审核记录
        record.setStatus("approved");
        feedbackMapper.updateById(record);
        
        return Response.success("审核成功");
    }
    
    @Override
    public Response<Void> editEmployment(Long counselorId, Long recordId, EmploymentRecord employmentRecord) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 验证辅导员是否有编辑权限
        Permission permission = permissionMapper.getPermissionByUserId(counselorId);
        if (permission == null || !permission.getCanEdit()) {
            return Response.error("无编辑权限");
        }
        
        // 查询就业记录
        EmploymentRecord record = employmentMapper.selectById(recordId);
        if (record == null) {
            return Response.error("就业记录不存在");
        }
        
        // 验证记录是否属于辅导员管理的学生
        boolean isManaged = isStudentManagedByCounselor(record.getStudentId(), counselorId);
        if (!isManaged) {
            return Response.error("无权限编辑此记录");
        }
        
        // 保存记录的原始学生ID和记录ID
        Long studentId = record.getStudentId();
        Long originalRecordId = record.getRecordId();
        
        // 更新记录
        employmentRecord.setStudentId(studentId);
        employmentRecord.setRecordId(originalRecordId);
        employmentMapper.updateById(employmentRecord);
        
        return Response.success("编辑成功");
    }
    
    @Override
    public Response<Void> deleteEmployment(Long counselorId, Long recordId) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 验证辅导员是否有删除权限
        Permission permission = permissionMapper.getPermissionByUserId(counselorId);
        if (permission == null || !permission.getCanDelete()) {
            return Response.error("无删除权限");
        }
        
        // 查询就业记录
        EmploymentRecord record = employmentMapper.selectById(recordId);
        if (record == null) {
            return Response.error("就业记录不存在");
        }
        
        // 验证记录是否属于辅导员管理的学生
        boolean isManaged = isStudentManagedByCounselor(record.getStudentId(), counselorId);
        if (!isManaged) {
            return Response.error("无权限删除此记录");
        }
        
        // 删除记录
        employmentMapper.deleteById(recordId);
        
        return Response.success("删除成功");
    }
    
    @Override
    public Response<Void> deleteFeedback(Long counselorId, Long feedbackId) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 验证辅导员是否有删除权限
        Permission permission = permissionMapper.getPermissionByUserId(counselorId);
        if (permission == null || !permission.getCanDelete()) {
            return Response.error("无删除权限");
        }
        
        // 查询反馈记录
        FeedbackRecord record = feedbackMapper.selectById(feedbackId);
        if (record == null) {
            return Response.error("反馈记录不存在");
        }
        
        // 验证记录是否属于辅导员管理的学生
        boolean isManaged = isStudentManagedByCounselor(record.getStudentId(), counselorId);
        if (!isManaged) {
            return Response.error("无权限删除此记录");
        }
        
        // 删除记录
        feedbackMapper.deleteById(feedbackId);
        
        return Response.success("删除成功");
    }
    
    @Override
    public Response<IPage<EmploymentRecord>> getHistoryEmployment(Long counselorId, Integer year, long current, long size) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 获取辅导员管理的班级历年就业信息
        Page<EmploymentRecord> page = new Page<>(current, size);
        IPage<EmploymentRecord> records = employmentMapper.getRecordsByCounselor(page, counselorId);
        return Response.success(records);
    }
    
    @Override
    public Response<List<Map<String, Object>>> getDistribution(Long counselorId, String type, Integer year) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 获取指定类型的分布统计
        List<Map<String, Object>> statistics;
        switch (type) {
            case "region":
                statistics = employmentMapper.getRegionDistribution(counselorId, null, year);
                break;
            case "salary":
                statistics = employmentMapper.getSalaryDistribution(counselorId, null, year);
                break;
            case "company_nature":
                statistics = employmentMapper.getCompanyNatureDistribution(counselorId, null, year);
                break;
            default:
                return Response.error("统计类型错误");
        }
        
        return Response.success(statistics);
    }
    
    @Override
    public Response<List<Map<String, Object>>> getTrend(Long counselorId, Integer startYear, Integer endYear) {
        // 验证辅导员是否存在
        Counselor counselor = counselorMapper.selectById(counselorId);
        if (counselor == null) {
            return Response.error("辅导员信息不存在");
        }
        
        // 获取辅导员管理的班级就业趋势
        List<Map<String, Object>> trend = employmentMapper.getEmploymentTrend(counselorId, null, startYear, endYear);
        return Response.success(trend);
    }
    
    /**
     * 验证学生是否由指定辅导员管理
     * @param studentId 学生ID
     * @param counselorId 辅导员ID
     * @return 是否由指定辅导员管理
     */
    private boolean isStudentManagedByCounselor(Long studentId, Long counselorId) {
        // 查询学生信息
        Student student = studentMapper.getStudentInfo(studentId);
        if (student == null) {
            return false;
        }
        
        // 验证学生是否由指定辅导员管理
        return counselorId.equals(student.getCounselorId());
    }
} 
