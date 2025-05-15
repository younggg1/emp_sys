package com.empsys.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.EmploymentRecord;
import com.empsys.backend.entity.FeedbackRecord;
import com.empsys.backend.entity.Student;
import com.empsys.backend.service.CounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;
    
    /**
     * 获取辅导员管理的学生列表
     */
    @GetMapping("/students")
    public Response<List<Student>> getStudents(@RequestParam Long counselorId) {
        return counselorService.getStudents(counselorId);
    }
    
    /**
     * 获取辅导员管理的学生就业信息
     */
    @GetMapping("/employment")
    public Response<IPage<EmploymentRecord>> getEmploymentRecords(
            @RequestParam Long counselorId,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        return counselorService.getEmploymentRecords(counselorId, current, size);
    }
    
    /**
     * 获取辅导员管理的学生反馈信息
     */
    @GetMapping("/feedback")
    public Response<IPage<FeedbackRecord>> getFeedbackRecords(
            @RequestParam Long counselorId,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        return counselorService.getFeedbackRecords(counselorId, current, size);
    }
    
    /**
     * 审核就业信息
     */
    @PostMapping("/employment/audit/{recordId}")
    public Response<Void> auditEmployment(
            @RequestParam Long counselorId,
            @PathVariable Long recordId) {
        return counselorService.auditEmployment(counselorId, recordId);
    }
    
    /**
     * 审核反馈信息
     */
    @PostMapping("/feedback/audit/{feedbackId}")
    public Response<Void> auditFeedback(
            @RequestParam Long counselorId,
            @PathVariable Long feedbackId) {
        return counselorService.auditFeedback(counselorId, feedbackId);
    }
    
    /**
     * 编辑就业信息（需权限）
     */
    @PutMapping("/employment/edit/{recordId}")
    public Response<Void> editEmployment(
            @RequestParam Long counselorId,
            @PathVariable Long recordId,
            @RequestBody EmploymentRecord employmentRecord) {
        return counselorService.editEmployment(counselorId, recordId, employmentRecord);
    }
    
    /**
     * 删除就业信息（需权限）
     */
    @DeleteMapping("/employment/delete/{recordId}")
    public Response<Void> deleteEmployment(
            @RequestParam Long counselorId,
            @PathVariable Long recordId) {
        return counselorService.deleteEmployment(counselorId, recordId);
    }
    
    /**
     * 删除反馈信息（需权限）
     */
    @DeleteMapping("/feedback/delete/{feedbackId}")
    public Response<Void> deleteFeedback(
            @RequestParam Long counselorId,
            @PathVariable Long feedbackId) {
        return counselorService.deleteFeedback(counselorId, feedbackId);
    }
    
    /**
     * 获取历年就业信息
     */
    @GetMapping("/statistics/history")
    public Response<IPage<EmploymentRecord>> getHistoryEmployment(
            @RequestParam Long counselorId,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        return counselorService.getHistoryEmployment(counselorId, year, current, size);
    }
    
    /**
     * 统计就业分布
     */
    @GetMapping("/statistics/distribution")
    public Response<List<Map<String, Object>>> getDistribution(
            @RequestParam Long counselorId,
            @RequestParam String type,
            @RequestParam(required = false) Integer year) {
        return counselorService.getDistribution(counselorId, type, year);
    }
    
    /**
     * 统计就业趋势
     */
    @GetMapping("/statistics/trend")
    public Response<List<Map<String, Object>>> getTrend(
            @RequestParam Long counselorId,
            @RequestParam Integer startYear,
            @RequestParam Integer endYear) {
        return counselorService.getTrend(counselorId, startYear, endYear);
    }
}
