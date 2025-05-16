package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.empsys.backend.entity.EmploymentRecords;
import com.empsys.backend.service.EmploymentRecordsService;
import com.empsys.backend.mapper.EmploymentRecordsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Quagmire
* @description 针对表【employment_records】的数据库操作Service实现
* @createDate 2025-05-16 17:49:01
*/
@Service
public class EmploymentRecordsServiceImpl extends ServiceImpl<EmploymentRecordsMapper, EmploymentRecords>
    implements EmploymentRecordsService{
    
    private static final Logger logger = LoggerFactory.getLogger(EmploymentRecordsServiceImpl.class);
    
    @Autowired
    private EmploymentRecordsMapper employmentRecordsMapper;
    
    @Override
    public List<EmploymentRecords> getStudentEmploymentRecords(Long studentId) {
        logger.info("获取学生ID为{}的就业信息列表", studentId);
        return employmentRecordsMapper.selectEmploymentRecordsByStudentId(studentId);
    }
    
    @Override
    public IPage<EmploymentRecords> getStudentEmploymentRecordsPage(Page<EmploymentRecords> page, Long studentId) {
        logger.info("分页获取学生ID为{}的就业信息列表，页码：{}，大小：{}", studentId, page.getCurrent(), page.getSize());
        return employmentRecordsMapper.selectEmploymentRecordsPageByStudentId(page, studentId);
    }
    
    @Override
    public boolean addEmploymentRecord(EmploymentRecords record) {
        logger.info("添加就业信息记录：{}", record);
        return employmentRecordsMapper.insertEmploymentRecord(record) > 0;
    }
    
    @Override
    public boolean updateEmploymentRecord(EmploymentRecords record) {
        logger.info("更新就业信息记录：{}", record);
        // 验证记录是否存在并且属于该学生
        int count = employmentRecordsMapper.countEmploymentRecordByIdAndStudentId(record.getRecord_id(), record.getStudent_id());
        if (count == 0) {
            logger.warn("就业信息记录不存在或不属于该学生，记录ID：{}，学生ID：{}", record.getRecord_id(), record.getStudent_id());
            return false;
        }
        
        return employmentRecordsMapper.updateEmploymentRecord(record) > 0;
    }
    
    @Override
    public boolean deleteEmploymentRecord(Long recordId, Long studentId) {
        logger.info("删除就业信息记录，ID：{}，学生ID：{}", recordId, studentId);
        // 验证记录是否存在并且属于该学生
        int count = employmentRecordsMapper.countEmploymentRecordByIdAndStudentId(recordId, studentId);
        if (count == 0) {
            logger.warn("就业信息记录不存在或不属于该学生，记录ID：{}，学生ID：{}", recordId, studentId);
            return false;
        }
        
        return employmentRecordsMapper.deleteEmploymentRecord(recordId) > 0;
    }
    
    @Override
    public EmploymentRecords getEmploymentRecordById(Long recordId) {
        logger.info("根据ID获取就业信息记录：{}", recordId);
        return employmentRecordsMapper.selectEmploymentRecordById(recordId);
    }
}




