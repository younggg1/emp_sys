package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.EmploymentRecord;
import com.empsys.backend.entity.FeedbackRecord;
import com.empsys.backend.entity.Student;
import com.empsys.backend.mapper.EmploymentMapper;
import com.empsys.backend.mapper.FeedbackMapper;
import com.empsys.backend.mapper.StudentMapper;
import com.empsys.backend.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private EmploymentMapper employmentMapper;
    
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    @Override
    public Response<Student> getStudentInfo(Long studentId) {
        // 使用自定义方法获取学生详细信息，包括辅导员姓名
        Student student = studentMapper.getStudentInfo(studentId);
        if (student == null) {
            return Response.error("学生信息不存在");
        }
        return Response.success(student);
    }
    
    @Override
    public Response<Map<String, Object>> registerEmployment(Long studentId, EmploymentRecord employmentRecord) {
        // 验证学生是否存在
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            return Response.error("学生信息不存在");
        }
        
        // 设置学生ID和其他必要字段
        employmentRecord.setStudentId(studentId);
        employmentRecord.setStatus("pending"); // 设置状态为待审核
        employmentRecord.setRecordId(System.currentTimeMillis()); // 使用时间戳作为ID
        
        employmentMapper.insert(employmentRecord);
        
        // 更新学生就业状态
        student.setEmploymentStatus("employed");
        studentMapper.updateById(student);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("record_id", employmentRecord.getRecordId());
        result.put("status", employmentRecord.getStatus());
        
        return Response.success("登记成功", result);
    }
    
    @Override
    public Response<Map<String, Object>> updateEmployment(Long studentId, Long recordId, EmploymentRecord employmentUpdate) {
        // 查询就业记录
        LambdaQueryWrapper<EmploymentRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EmploymentRecord::getStudentId, studentId)
                .eq(EmploymentRecord::getRecordId, recordId);
        
        EmploymentRecord record = employmentMapper.selectOne(queryWrapper);
        if (record == null) {
            return Response.error("就业记录不存在");
        }
        
        // 验证记录是否处于待审核状态
        if (!"pending".equals(record.getStatus())) {
            return Response.error("已审核的记录不可修改");
        }
        
        // 保留必要字段
        Long oldRecordId = record.getRecordId();
        Long oldStudentId = record.getStudentId();
        String oldStatus = record.getStatus();
        
        // 更新记录
        BeanUtils.copyProperties(employmentUpdate, record);
        
        // 恢复必要字段
        record.setRecordId(oldRecordId);
        record.setStudentId(oldStudentId);
        record.setStatus(oldStatus);
        
        employmentMapper.updateById(record);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("record_id", record.getRecordId());
        result.put("status", record.getStatus());
        
        return Response.success("更新成功", result);
    }
    
    @Override
    public Response<Void> deleteEmployment(Long studentId, Long recordId) {
        // 查询就业记录
        LambdaQueryWrapper<EmploymentRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EmploymentRecord::getStudentId, studentId)
                .eq(EmploymentRecord::getRecordId, recordId);
        
        EmploymentRecord record = employmentMapper.selectOne(queryWrapper);
        if (record == null) {
            return Response.error("就业记录不存在");
        }
        
        // 验证记录是否处于待审核状态
        if (!"pending".equals(record.getStatus())) {
            return Response.error("已审核的记录不可删除");
        }
        
        // 删除记录
        employmentMapper.delete(queryWrapper);
        
        // 检查是否还有其他就业记录
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EmploymentRecord::getStudentId, studentId);
        int count = employmentMapper.selectCount(queryWrapper).intValue();
        
        // 如果没有其他记录，更新学生就业状态为未就业
        if (count == 0) {
            Student student = studentMapper.selectById(studentId);
            if (student != null) {
                student.setEmploymentStatus("unemployed");
                studentMapper.updateById(student);
            }
        }
        
        return Response.success("删除成功");
    }
    
    @Override
    public Response<List<EmploymentRecord>> getEmploymentRecords(Long studentId) {
        List<EmploymentRecord> records = employmentMapper.getRecordsByStudent(studentId);
        return Response.success(records);
    }
    
    @Override
    public Response<Map<String, Object>> submitFeedback(Long studentId, FeedbackRecord feedbackRecord) {
        // 验证学生是否存在
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            return Response.error("学生信息不存在");
        }
        
        // 验证学生是否已就业
        if (!"employed".equals(student.getEmploymentStatus())) {
            return Response.error("未就业学生不能提交反馈");
        }
        
        // 设置学生ID和其他必要字段
        feedbackRecord.setStudentId(studentId);
        feedbackRecord.setStatus("pending"); // 设置状态为待审核
        feedbackRecord.setFeedbackId(System.currentTimeMillis()); // 使用时间戳作为ID
        
        feedbackMapper.insert(feedbackRecord);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("feedback_id", feedbackRecord.getFeedbackId());
        result.put("status", feedbackRecord.getStatus());
        
        return Response.success("提交成功", result);
    }
    
    @Override
    public Response<Map<String, Object>> updateFeedback(Long studentId, Long feedbackId, FeedbackRecord feedbackUpdate) {
        // 查询反馈记录
        LambdaQueryWrapper<FeedbackRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FeedbackRecord::getStudentId, studentId)
                .eq(FeedbackRecord::getFeedbackId, feedbackId);
        
        FeedbackRecord record = feedbackMapper.selectOne(queryWrapper);
        if (record == null) {
            return Response.error("反馈记录不存在");
        }
        
        // 验证记录是否处于待审核状态
        if (!"pending".equals(record.getStatus())) {
            return Response.error("已审核的反馈不可修改");
        }
        
        // 保留必要字段
        Long oldFeedbackId = record.getFeedbackId();
        Long oldStudentId = record.getStudentId();
        String oldStatus = record.getStatus();
        
        // 更新记录
        BeanUtils.copyProperties(feedbackUpdate, record);
        
        // 恢复必要字段
        record.setFeedbackId(oldFeedbackId);
        record.setStudentId(oldStudentId);
        record.setStatus(oldStatus);
        
        feedbackMapper.updateById(record);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("feedback_id", record.getFeedbackId());
        result.put("status", record.getStatus());
        
        return Response.success("更新成功", result);
    }
    
    @Override
    public Response<Void> deleteFeedback(Long studentId, Long feedbackId) {
        // 查询反馈记录
        LambdaQueryWrapper<FeedbackRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FeedbackRecord::getStudentId, studentId)
                .eq(FeedbackRecord::getFeedbackId, feedbackId);
        
        FeedbackRecord record = feedbackMapper.selectOne(queryWrapper);
        if (record == null) {
            return Response.error("反馈记录不存在");
        }
        
        // 验证记录是否处于待审核状态
        if (!"pending".equals(record.getStatus())) {
            return Response.error("已审核的反馈不可删除");
        }
        
        // 删除记录
        feedbackMapper.delete(queryWrapper);
        
        return Response.success("删除成功");
    }
    
    @Override
    public Response<List<FeedbackRecord>> getFeedbackRecords(Long studentId) {
        List<FeedbackRecord> records = feedbackMapper.getFeedbackByStudent(studentId);
        return Response.success(records);
    }
} 
