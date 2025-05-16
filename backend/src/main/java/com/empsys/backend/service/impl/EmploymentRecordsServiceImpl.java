package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.empsys.backend.entity.EmploymentRecords;
import com.empsys.backend.entity.Students;
import com.empsys.backend.mapper.StudentsMapper;
import com.empsys.backend.service.EmploymentRecordsService;
import com.empsys.backend.mapper.EmploymentRecordsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private StudentsMapper studentsMapper;
    
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
    
    @Override
    public List<Map<String, Object>> getEmploymentRecordsByCounselorId(Long counselorId) {
        logger.info("获取辅导员ID为{}的学生就业信息列表", counselorId);
        return employmentRecordsMapper.selectEmploymentRecordsByCounselorId(counselorId);
    }

    @Override
    public boolean approveEmploymentRecord(Long recordId, String status, Long counselorId) {
        logger.info("辅导员审核就业信息，记录ID：{}，状态：{}，辅导员ID：{}", recordId, status, counselorId);

        // 验证记录是否存在且属于该辅导员负责的学生
        int count = employmentRecordsMapper.countEmploymentRecordByIdAndCounselorId(recordId, counselorId);
        if (count == 0) {
            logger.warn("就业信息记录不存在或不属于该辅导员负责的学生，记录ID：{}，辅导员ID：{}", recordId, counselorId);
            return false;
        }

        // 获取完整的就业记录，以便获取学生ID
        EmploymentRecords fullRecord = employmentRecordsMapper.selectEmploymentRecordById(recordId);
        if (fullRecord == null) {
            logger.warn("就业信息记录不存在，记录ID：{}", recordId);
            return false;
        }

        // 更新就业记录状态
        EmploymentRecords record = new EmploymentRecords();
        record.setRecord_id(recordId);
        record.setStatus(status);
        record.setUpdated_at(new Date());

        boolean updateSuccess = employmentRecordsMapper.updateEmploymentRecord(record) > 0;

        // 如果审核通过，则更新学生的就业状态
        if (updateSuccess && "approved".equals(status)) {
            Students student = new Students();
            student.setStudent_id(fullRecord.getStudent_id());
            student.setEmployment_status("employed");
            student.setUpdated_at(new Date());
            studentsMapper.updateById(student);
            logger.info("已更新学生ID为{}的就业状态为已就业", fullRecord.getStudent_id());
        }

        return updateSuccess;
    }
    

    
    @Override
    public boolean deleteEmploymentRecordByCounselor(Long recordId, Long counselorId) {
        logger.info("辅导员删除就业信息，记录ID：{}，辅导员ID：{}", recordId, counselorId);
        
        // 验证记录是否存在且属于该辅导员负责的学生
        int count = employmentRecordsMapper.countEmploymentRecordByIdAndCounselorId(recordId, counselorId);
        if (count == 0) {
            logger.warn("就业信息记录不存在或不属于该辅导员负责的学生，记录ID：{}，辅导员ID：{}", recordId, counselorId);
            return false;
        }
        
        return employmentRecordsMapper.deleteEmploymentRecord(recordId) > 0;
    }
}




