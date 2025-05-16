package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.empsys.backend.entity.Students;
import com.empsys.backend.service.StudentsService;
import com.empsys.backend.mapper.StudentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Quagmire
* @description 针对表【students】的数据库操作Service实现
* @createDate 2025-05-16 17:12:31
*/
@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students>
    implements StudentsService{

    private static final Logger logger = LoggerFactory.getLogger(StudentsServiceImpl.class);
    
    @Autowired
    private StudentsMapper studentsMapper;

    @Override
    public Students getStudentInfo(Long userId) {
        logger.info("尝试获取用户ID为{}的学生信息", userId);
        
        // 调用mapper查询学生信息
        Students studentInfo = studentsMapper.selectStudentInfoByUserId(userId);
        
        if (studentInfo != null) {
            logger.info("找到学生信息: {}", studentInfo);
        } else {
            logger.warn("未找到用户ID为{}的学生信息", userId);
        }
        
        return studentInfo;
    }
}




