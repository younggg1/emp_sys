package com.empsys.backend.controller;

import com.empsys.backend.entity.Response;
import com.empsys.backend.entity.EmploymentRecord;
import com.empsys.backend.entity.FeedbackRecord;
import com.empsys.backend.entity.Student;
import com.empsys.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    /**
     * 获取学生信息
     */
    @GetMapping("/student/info")
    public Response<Student> getStudentInfo(@RequestParam Long studentId) {
        return studentService.getStudentInfo(studentId);
    }
    
    /**
     * 登记就业信息
     */
    @PostMapping("/employment/register")
    public Response<Map<String, Object>> registerEmployment(@RequestParam Long studentId, @RequestBody EmploymentRecord employmentRecord) {
        return studentService.registerEmployment(studentId, employmentRecord);
    }
    
    /**
     * 更新就业信息
     */
    @PutMapping("/employment/update/{recordId}")
    public Response<Map<String, Object>> updateEmployment(@RequestParam Long studentId, @PathVariable Long recordId, @RequestBody EmploymentRecord employmentRecord) {
        return studentService.updateEmployment(studentId, recordId, employmentRecord);
    }
    
    /**
     * 删除就业信息
     */
    @DeleteMapping("/employment/delete/{recordId}")
    public Response<Void> deleteEmployment(@RequestParam Long studentId, @PathVariable Long recordId) {
        return studentService.deleteEmployment(studentId, recordId);
    }
    
    /**
     * 获取学生就业信息
     */
    @GetMapping("/student/employment")
    public Response<List<EmploymentRecord>> getEmploymentRecords(@RequestParam Long studentId) {
        return studentService.getEmploymentRecords(studentId);
    }
    
    /**
     * 提交就业反馈
     */
    @PostMapping("/feedback/submit")
    public Response<Map<String, Object>> submitFeedback(@RequestParam Long studentId, @RequestBody FeedbackRecord feedbackRecord) {
        return studentService.submitFeedback(studentId, feedbackRecord);
    }
    
    /**
     * 更新就业反馈
     */
    @PutMapping("/feedback/update/{feedbackId}")
    public Response<Map<String, Object>> updateFeedback(@RequestParam Long studentId, @PathVariable Long feedbackId, @RequestBody FeedbackRecord feedbackRecord) {
        return studentService.updateFeedback(studentId, feedbackId, feedbackRecord);
    }
    
    /**
     * 删除就业反馈
     */
    @DeleteMapping("/feedback/delete/{feedbackId}")
    public Response<Void> deleteFeedback(@RequestParam Long studentId, @PathVariable Long feedbackId) {
        return studentService.deleteFeedback(studentId, feedbackId);
    }
    
    /**
     * 获取学生反馈信息
     */
    @GetMapping("/student/feedback")
    public Response<List<FeedbackRecord>> getFeedbackRecords(@RequestParam Long studentId) {
        return studentService.getFeedbackRecords(studentId);
    }
}
