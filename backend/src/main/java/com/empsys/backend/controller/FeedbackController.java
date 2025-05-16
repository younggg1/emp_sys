package com.empsys.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.config.Result;
import com.empsys.backend.entity.FeedbackRecords;
import com.empsys.backend.service.FeedbackRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 就业反馈信息控制器
 */
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackRecordsService feedbackRecordsService;

    /**
     * 获取学生反馈列表
     * @param studentId 学生ID
     * @return 反馈列表
     */
    @GetMapping("/list")
    public Result<List<FeedbackRecords>> getFeedbackRecords(@RequestParam Long studentId) {
        logger.info("获取学生ID为{}的反馈列表", studentId);
        List<FeedbackRecords> records = feedbackRecordsService.getStudentFeedbackRecords(studentId);
        return Result.success(records);
    }

    /**
     * 分页获取学生反馈列表
     * @param current 当前页码
     * @param size 每页大小
     * @param studentId 学生ID
     * @return 分页后的反馈列表
     */
    @GetMapping("/page")
    public Result<IPage<FeedbackRecords>> getFeedbackRecordsPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam Long studentId) {
        logger.info("分页获取学生ID为{}的反馈列表，页码：{}，大小：{}", studentId, current, size);
        Page<FeedbackRecords> page = new Page<>(current, size);
        IPage<FeedbackRecords> records = feedbackRecordsService.getStudentFeedbackRecordsPage(page, studentId);
        return Result.success(records);
    }

    /**
     * 获取反馈详情
     * @param feedbackId 反馈ID
     * @return 反馈详情
     */
    @GetMapping("/detail/{feedbackId}")
    public Result<FeedbackRecords> getFeedbackRecordDetail(@PathVariable Long feedbackId) {
        logger.info("获取反馈详情，记录ID：{}", feedbackId);
        FeedbackRecords record = feedbackRecordsService.getFeedbackRecordById(feedbackId);
        if (record == null) {
            return Result.error("反馈记录不存在");
        }
        return Result.success(record);
    }

    /**
     * 添加反馈
     * @param record 反馈记录
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result<FeedbackRecords> addFeedbackRecord(@RequestBody FeedbackRecords record) {
        logger.info("添加反馈记录：{}", record);
        
        // 设置默认状态为待审核
        record.setStatus("pending");
        record.setCreated_at(new Date());
        record.setUpdated_at(new Date());
        
        boolean success = feedbackRecordsService.addFeedbackRecord(record);
        if (success) {
            Result<FeedbackRecords> result = Result.success(record);
            result.setMessage("反馈添加成功");
            return result;
        } else {
            return Result.error("反馈添加失败");
        }
    }

    /**
     * 更新反馈
     * @param record 反馈记录
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<FeedbackRecords> updateFeedbackRecord(@RequestBody FeedbackRecords record) {
        logger.info("更新反馈记录：{}", record);
        
        if (record.getFeedback_id() == null) {
            return Result.error("记录ID不能为空");
        }
        
        // 设置更新时间和状态为待审核
        record.setStatus("pending");
        record.setUpdated_at(new Date());
        
        boolean success = feedbackRecordsService.updateFeedbackRecord(record);
        if (success) {
            Result<FeedbackRecords> result = Result.success(record);
            result.setMessage("反馈更新成功");
            return result;
        } else {
            return Result.error("反馈更新失败，记录不存在或无权限");
        }
    }

    /**
     * 删除反馈
     * @param feedbackId 反馈ID
     * @param studentId 学生ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{feedbackId}")
    public Result<Boolean> deleteFeedbackRecord(
            @PathVariable Long feedbackId,
            @RequestParam Long studentId) {
        logger.info("删除反馈记录，记录ID：{}，学生ID：{}", feedbackId, studentId);
        boolean success = feedbackRecordsService.deleteFeedbackRecord(feedbackId, studentId);
        if (success) {
            Result<Boolean> result = Result.success(true);
            result.setMessage("反馈删除成功");
            return result;
        } else {
            return Result.error("反馈删除失败，记录不存在或无权限");
        }
    }
    
    /**
     * 审核反馈（仅限辅导员和管理员）
     * @param feedbackId 反馈ID
     * @param status 状态（approved/rejected）
     * @return 审核结果
     */
    @PutMapping("/approve/{feedbackId}")
    public Result<Boolean> approveFeedbackRecord(
            @PathVariable Long feedbackId,
            @RequestParam String status) {
        logger.info("审核反馈记录，记录ID：{}，状态：{}", feedbackId, status);
        
        if (!status.equals("approved") && !status.equals("rejected")) {
            return Result.error("状态值无效，只能是 approved 或 rejected");
        }
        
        FeedbackRecords record = feedbackRecordsService.getFeedbackRecordById(feedbackId);
        if (record == null) {
            return Result.error("反馈记录不存在");
        }
        
        record.setStatus(status);
        record.setUpdated_at(new Date());
        boolean success = feedbackRecordsService.updateFeedbackRecord(record);
        
        if (success) {
            Result<Boolean> result = Result.success(true);
            result.setMessage("反馈审核成功");
            return result;
        } else {
            return Result.error("反馈审核失败");
        }
    }
} 