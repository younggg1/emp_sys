package com.empsys.backend.service;

import com.empsys.backend.entity.Students;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Quagmire
* @description 针对表【students】的数据库操作Service
* @createDate 2025-05-16 17:12:31
*/
public interface StudentsService extends IService<Students> {
    
    /**
     * 根据用户ID获取学生信息
     * @param userId 用户ID
     * @return 学生信息
     */
    Students getStudentInfo(Long userId);
}
