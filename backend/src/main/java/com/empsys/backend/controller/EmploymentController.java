package com.empsys.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.config.Result;
import com.empsys.backend.entity.EmploymentRecords;
import com.empsys.backend.service.EmploymentRecordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 就业信息控制器
 */
@RestController
@RequestMapping("/api/employment")
public class EmploymentController {

    private static final Logger logger = LoggerFactory.getLogger(EmploymentController.class);

    @Autowired
    private EmploymentRecordsService employmentRecordsService;

    /**
     * 获取学生就业信息列表
     * @param studentId 学生ID
     * @return 就业信息列表
     */
    @GetMapping("/list")
    public Result<List<EmploymentRecords>> getEmploymentRecords(@RequestParam Long studentId) {
        logger.info("获取学生ID为{}的就业信息列表", studentId);
        List<EmploymentRecords> records = employmentRecordsService.getStudentEmploymentRecords(studentId);
        return Result.success(records);
    }

    /**
     * 分页获取学生就业信息列表
     * @param current 当前页码
     * @param size 每页大小
     * @param studentId 学生ID
     * @return 分页后的就业信息列表
     */
    @GetMapping("/page")
    public Result<IPage<EmploymentRecords>> getEmploymentRecordsPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam Long studentId) {
        logger.info("分页获取学生ID为{}的就业信息列表，页码：{}，大小：{}", studentId, current, size);
        Page<EmploymentRecords> page = new Page<>(current, size);
        IPage<EmploymentRecords> records = employmentRecordsService.getStudentEmploymentRecordsPage(page, studentId);
        return Result.success(records);
    }

    /**
     * 获取就业信息详情
     * @param recordId 记录ID
     * @return 就业信息详情
     */
    @GetMapping("/detail/{recordId}")
    public Result<EmploymentRecords> getEmploymentRecordDetail(@PathVariable Long recordId) {
        logger.info("获取就业信息详情，记录ID：{}", recordId);
        EmploymentRecords record = employmentRecordsService.getEmploymentRecordById(recordId);
        if (record == null) {
            return Result.error("就业信息记录不存在");
        }
        return Result.success(record);
    }

    /**
     * 添加就业信息
     * @param record 就业信息记录
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result<EmploymentRecords> addEmploymentRecord(@RequestBody EmploymentRecords record) {
        logger.info("添加就业信息记录：{}", record);
        
        // 设置默认状态为待审核
        record.setStatus("pending");
        record.setCreated_at(new Date());
        record.setUpdated_at(new Date());
        
        boolean success = employmentRecordsService.addEmploymentRecord(record);
        if (success) {
            Result<EmploymentRecords> result = Result.success(record);
            result.setMessage("就业信息添加成功");
            return result;
        } else {
            return Result.error("就业信息添加失败");
        }
    }

    /**
     * 更新就业信息
     * @param record 就业信息记录
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<EmploymentRecords> updateEmploymentRecord(@RequestBody EmploymentRecords record) {
        logger.info("更新就业信息记录：{}", record);
        
        if (record.getRecord_id() == null) {
            return Result.error("记录ID不能为空");
        }
        
        // 设置更新时间和状态为待审核
        record.setStatus("pending");
        record.setUpdated_at(new Date());
        
        boolean success = employmentRecordsService.updateEmploymentRecord(record);
        if (success) {
            Result<EmploymentRecords> result = Result.success(record);
            result.setMessage("就业信息更新成功");
            return result;
        } else {
            return Result.error("就业信息更新失败，记录不存在或无权限");
        }
    }

    /**
     * 删除就业信息
     * @param recordId 记录ID
     * @param studentId 学生ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{recordId}")
    public Result<Boolean> deleteEmploymentRecord(
            @PathVariable Long recordId,
            @RequestParam Long studentId) {
        logger.info("删除就业信息记录，记录ID：{}，学生ID：{}", recordId, studentId);
        boolean success = employmentRecordsService.deleteEmploymentRecord(recordId, studentId);
        if (success) {
            Result<Boolean> result = Result.success(true);
            result.setMessage("就业信息删除成功");
            return result;
        } else {
            return Result.error("就业信息删除失败，记录不存在或无权限");
        }
    }
    
    /**
     * 审核就业信息（仅限辅导员和管理员）
     * @param recordId 记录ID
     * @param status 状态（approved/rejected）
     * @return 审核结果
     */
    @PutMapping("/approve/{recordId}")
    public Result<Boolean> approveEmploymentRecord(
            @PathVariable Long recordId,
            @RequestParam String status) {
        logger.info("审核就业信息记录，记录ID：{}，状态：{}", recordId, status);
        
        if (!status.equals("approved") && !status.equals("rejected")) {
            return Result.error("状态值无效，只能是 approved 或 rejected");
        }
        
        EmploymentRecords record = employmentRecordsService.getEmploymentRecordById(recordId);
        if (record == null) {
            return Result.error("就业信息记录不存在");
        }
        
        record.setStatus(status);
        record.setUpdated_at(new Date());
        boolean success = employmentRecordsService.updateEmploymentRecord(record);
        
        if (success) {
            Result<Boolean> result = Result.success(true);
            result.setMessage("就业信息审核成功");
            return result;
        } else {
            return Result.error("就业信息审核失败");
        }
    }
} 