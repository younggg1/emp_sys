package com.empsys.backend.controller;

import com.empsys.backend.config.Result;
import com.empsys.backend.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/history")
public class AdminHistoryController {

    @Autowired
    private HistoryService historyService;

    // 查询所有就业信息（历年就业明细）
    @GetMapping
    public Result<List<Map<String, Object>>> findAll() {
        try {
            List<Map<String, Object>> data = historyService.findAll();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取历年就业数据失败");
        }
    }

    // 删除就业信息
    @DeleteMapping("/{recordId}")
    public Result<Boolean> delete(@PathVariable Long recordId) {
        boolean success = historyService.deleteById(recordId);
        if (success) {
            Result<Boolean> result = Result.success(true);
            result.setMessage("就业信息删除成功");
            return result;
        } else {
            return Result.error("就业信息删除失败，记录不存在或无权限");
        }
    }

}