package com.empsys.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.config.Result;
import com.empsys.backend.entity.Counselors;
import com.empsys.backend.entity.EmploymentRecords;
import com.empsys.backend.entity.FeedbackRecords;
import com.empsys.backend.entity.Students;
import com.empsys.backend.service.CounselorsService;
import com.empsys.backend.service.EmploymentRecordsService;
import com.empsys.backend.service.FeedbackRecordsService;
import com.empsys.backend.service.StudentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 辅导员控制器
 */
@RestController
@RequestMapping("/api/counselor")
public class CounselorController {

    private static final Logger logger = LoggerFactory.getLogger(CounselorController.class);

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private FeedbackRecordsService feedbackRecordsService;
    
    @Autowired
    private CounselorsService counselorsService;
    
    @Autowired
    private EmploymentRecordsService employmentRecordsService;

    /**
     * 获取辅导员负责班级的学生列表
     * @param counselorId 辅导员ID
     * @return 学生列表
     */
    @GetMapping("/students")
    public Result<List<Students>> getStudents(@RequestParam Long counselorId) {
        logger.info("获取辅导员ID为{}的学生列表", counselorId);
        List<Students> students = studentsService.getStudentsByCounselorId(counselorId);
        return Result.success(students);
    }

    /**
     * 获取辅导员负责班级的学生反馈列表
     * @param counselorId 辅导员ID
     * @return 反馈列表
     */
    @GetMapping("/feedback")
    public Result<List<Map<String, Object>>> getFeedbackRecords(@RequestParam Long counselorId) {
        logger.info("获取辅导员ID为{}的学生反馈列表", counselorId);
        List<Map<String, Object>> feedbackList = feedbackRecordsService.getFeedbackRecordsByCounselorId(counselorId);
        return Result.success(feedbackList);
    }

    /**
     * 分页获取辅导员负责班级的学生反馈列表
     * @param current 当前页码
     * @param size 每页大小
     * @param counselorId 辅导员ID
     * @return 分页后的反馈列表
     */
    @GetMapping("/feedback/page")
    public Result<IPage<Map<String, Object>>> getFeedbackRecordsPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam Long counselorId) {
        logger.info("分页获取辅导员ID为{}的学生反馈列表，页码：{}，大小：{}", counselorId, current, size);
        Page<Map<String, Object>> page = new Page<>(current, size);
        IPage<Map<String, Object>> records = feedbackRecordsService.getFeedbackRecordsPageByCounselorId(page, counselorId);
        return Result.success(records);
    }

    /**
     * 审核反馈
     * @param feedbackId 反馈ID
     * @param status 状态（approved/rejected）
     * @param counselorId 辅导员ID
     * @return 审核结果
     */
    @PutMapping("/feedback/approve/{feedbackId}")
    public Result<Boolean> approveFeedbackRecord(
            @PathVariable Long feedbackId,
            @RequestParam String status,
            @RequestParam Long counselorId) {
        logger.info("审核反馈记录，记录ID：{}，状态：{}，辅导员ID：{}", feedbackId, status, counselorId);
        
        if (!status.equals("approved") && !status.equals("rejected")) {
            return Result.error("状态值无效，只能是 approved 或 rejected");
        }
        
        boolean success = feedbackRecordsService.approveFeedbackRecord(feedbackId, status, counselorId);
        if (success) {
            Result<Boolean> result = Result.success(true);
            result.setMessage("反馈审核成功");
            return result;
        } else {
            return Result.error("反馈审核失败，反馈不存在或无权限操作");
        }
    }

    /**
     * 删除反馈
     * @param feedbackId 反馈ID
     * @param counselorId 辅导员ID
     * @return 删除结果
     */
    @DeleteMapping("/feedback/{feedbackId}")
    public Result<Boolean> deleteFeedbackRecord(
            @PathVariable Long feedbackId,
            @RequestParam Long counselorId) {
        logger.info("删除反馈记录，记录ID：{}，辅导员ID：{}", feedbackId, counselorId);
        
        boolean success = feedbackRecordsService.deleteFeedbackRecordByCounselor(feedbackId, counselorId);
        if (success) {
            Result<Boolean> result = Result.success(true);
            result.setMessage("反馈删除成功");
            return result;
        } else {
            return Result.error("反馈删除失败，记录不存在或无权限");
        }
    }

    /**
     * 获取反馈详情
     * @param feedbackId 反馈ID
     * @param counselorId 辅导员ID
     * @return 反馈详情
     */
    @GetMapping("/feedback/detail/{feedbackId}")
    public Result<Map<String, Object>> getFeedbackRecordDetail(
            @PathVariable Long feedbackId,
            @RequestParam Long counselorId) {
        logger.info("获取反馈详情，记录ID：{}，辅导员ID：{}", feedbackId, counselorId);
        
        Map<String, Object> record = feedbackRecordsService.getFeedbackRecordDetailByCounselor(feedbackId, counselorId);
        if (record == null) {
            return Result.error("反馈记录不存在或无权限查看");
        }
        return Result.success(record);
    }
    
    /**
     * 获取辅导员负责班级的学生就业信息列表
     * @param counselorId 辅导员ID
     * @return 就业信息列表
     */
    @GetMapping("/employment")
    public Result<List<Map<String, Object>>> getEmploymentRecords(@RequestParam Long counselorId) {
        logger.info("获取辅导员ID为{}的学生就业信息列表", counselorId);
        List<Map<String, Object>> employmentList = employmentRecordsService.getEmploymentRecordsByCounselorId(counselorId);
        return Result.success(employmentList);
    }
    
    /**
     * 审核就业信息
     * @param recordId 就业信息ID
     * @param status 状态（approved/rejected）
     * @param counselorId 辅导员ID
     * @return 审核结果
     */
    @PutMapping("/employment/approve/{recordId}")
    public Result<Boolean> approveEmploymentRecord(
            @PathVariable Long recordId,
            @RequestParam String status,
            @RequestParam Long counselorId) {
        logger.info("审核就业信息，记录ID：{}，状态：{}，辅导员ID：{}", recordId, status, counselorId);
        
        if (!status.equals("approved") && !status.equals("rejected")) {
            return Result.error("状态值无效，只能是 approved 或 rejected");
        }
        
        boolean success = employmentRecordsService.approveEmploymentRecord(recordId, status, counselorId);
        if (success) {
            Result<Boolean> result = Result.success(true);
            result.setMessage("就业信息审核成功");
            return result;
        } else {
            return Result.error("就业信息审核失败，记录不存在或无权限操作");
        }
    }
    
    /**
     * 辅导员删除就业信息
     * @param recordId 就业信息ID
     * @param counselorId 辅导员ID
     * @return 删除结果
     */
    @DeleteMapping("/employment/{recordId}")
    public Result<Boolean> deleteEmploymentRecord(
            @PathVariable Long recordId,
            @RequestParam Long counselorId) {
        logger.info("辅导员删除就业信息，记录ID：{}，辅导员ID：{}", recordId, counselorId);
        
        boolean success = employmentRecordsService.deleteEmploymentRecordByCounselor(recordId, counselorId);
        if (success) {
            Result<Boolean> result = Result.success(true);
            result.setMessage("就业信息删除成功");
            return result;
        } else {
            return Result.error("就业信息删除失败，记录不存在或无权限操作");
        }
    }


    /**
     * 更新辅导员权限
     * @param counselor_id 辅导员ID
     * @param permission 权限值（Y-可删除，N-不可删除）
     * @return 更新结果
     */
    @PutMapping("/updatePermission/{counselor_id}")
    public Result<Boolean> updatePermission(
            @PathVariable Long counselor_id,
            @RequestParam String permission) {
        logger.info("更新辅导员权限，辅导员ID：{}，权限值：{}", counselor_id, permission);
        
        if (!permission.equals("Y") && !permission.equals("N")) {
            logger.error("无效的权限值：{}", permission);
            return Result.error("权限值无效，只能是Y或N");
        }
        
        try {
            boolean success = counselorsService.updatePermission(counselor_id, permission);
            if (success) {
                logger.info("权限更新成功");
                return Result.success(true);
            } else {
                logger.error("权限更新失败，可能是辅导员不存在");
                return Result.error("权限更新失败，辅导员不存在");
            }
        } catch (Exception e) {
            logger.error("权限更新时发生异常", e);
            return Result.error("权限更新失败：" + e.getMessage());
        }
    }

    /**
     * 获取辅导员完整信息列表（包含工号）
     * @return 辅导员列表
     */
    @GetMapping("/listWithUserInfo")
    public Result<List<Map<String, Object>>> getCounselorListWithUserInfo() {
        logger.info("获取辅导员完整信息列表");
        List<Map<String, Object>> counselors = counselorsService.getCounselorListWithUserInfo();
        return Result.success(counselors);
    }
} 