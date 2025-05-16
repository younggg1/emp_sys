package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.empsys.backend.entity.FeedbackRecords;
import com.empsys.backend.service.FeedbackRecordsService;
import com.empsys.backend.mapper.FeedbackRecordsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author Quagmire
* @description 针对表【feedback_records】的数据库操作Service实现
* @createDate 2025-05-16 18:10:11
*/
@Service
public class FeedbackRecordsServiceImpl extends ServiceImpl<FeedbackRecordsMapper, FeedbackRecords>
    implements FeedbackRecordsService{

    private static final Logger logger = LoggerFactory.getLogger(FeedbackRecordsServiceImpl.class);
    
    @Autowired
    private FeedbackRecordsMapper feedbackRecordsMapper;

    @Override
    public List<FeedbackRecords> getStudentFeedbackRecords(Long studentId) {
        logger.info("获取学生ID为{}的反馈列表", studentId);
        return feedbackRecordsMapper.selectFeedbackRecordsByStudentId(studentId);
    }

    @Override
    public IPage<FeedbackRecords> getStudentFeedbackRecordsPage(Page<FeedbackRecords> page, Long studentId) {
        logger.info("分页获取学生ID为{}的反馈列表，页码：{}，大小：{}", studentId, page.getCurrent(), page.getSize());
        return feedbackRecordsMapper.selectFeedbackRecordsPageByStudentId(page, studentId);
    }

    @Override
    public boolean addFeedbackRecord(FeedbackRecords record) {
        logger.info("添加反馈记录：{}", record);
        return feedbackRecordsMapper.insertFeedbackRecord(record) > 0;
    }

    @Override
    public boolean updateFeedbackRecord(FeedbackRecords record) {
        logger.info("更新反馈记录：{}", record);
        // 验证记录是否存在并且属于该学生
        int count = feedbackRecordsMapper.countFeedbackRecordByIdAndStudentId(record.getFeedback_id(), record.getStudent_id());
        if (count == 0) {
            logger.warn("反馈记录不存在或不属于该学生，记录ID：{}，学生ID：{}", record.getFeedback_id(), record.getStudent_id());
            return false;
        }
        
        return feedbackRecordsMapper.updateFeedbackRecord(record) > 0;
    }

    @Override
    public boolean deleteFeedbackRecord(Long feedbackId, Long studentId) {
        logger.info("删除反馈记录，ID：{}，学生ID：{}", feedbackId, studentId);
        // 验证记录是否存在并且属于该学生
        int count = feedbackRecordsMapper.countFeedbackRecordByIdAndStudentId(feedbackId, studentId);
        if (count == 0) {
            logger.warn("反馈记录不存在或不属于该学生，记录ID：{}，学生ID：{}", feedbackId, studentId);
            return false;
        }
        
        return feedbackRecordsMapper.deleteFeedbackRecord(feedbackId) > 0;
    }

    @Override
    public FeedbackRecords getFeedbackRecordById(Long feedbackId) {
        logger.info("根据ID获取反馈记录：{}", feedbackId);
        return feedbackRecordsMapper.selectFeedbackRecordById(feedbackId);
    }
    
    @Override
    public List<Map<String, Object>> getFeedbackRecordsByCounselorId(Long counselorId) {
        logger.info("获取辅导员ID为{}负责学生的反馈列表", counselorId);
        return feedbackRecordsMapper.selectFeedbackRecordsByCounselorId(counselorId);
    }
    
    @Override
    public IPage<Map<String, Object>> getFeedbackRecordsPageByCounselorId(Page<Map<String, Object>> page, Long counselorId) {
        logger.info("分页获取辅导员ID为{}负责学生的反馈列表，页码：{}，大小：{}", counselorId, page.getCurrent(), page.getSize());
        return feedbackRecordsMapper.selectFeedbackRecordsPageByCounselorId(page, counselorId);
    }
    
    @Override
    public boolean approveFeedbackRecord(Long feedbackId, String status, Long counselorId) {
        logger.info("辅导员审核反馈记录，反馈ID：{}，状态：{}，辅导员ID：{}", feedbackId, status, counselorId);
        // 验证记录是否存在并且属于辅导员负责的学生
        int count = feedbackRecordsMapper.countFeedbackRecordByIdAndCounselorId(feedbackId, counselorId);
        if (count == 0) {
            logger.warn("反馈记录不存在或不属于辅导员负责的学生，记录ID：{}，辅导员ID：{}", feedbackId, counselorId);
            return false;
        }
        
        // 获取反馈记录
        FeedbackRecords record = feedbackRecordsMapper.selectFeedbackRecordById(feedbackId);
        if (record == null) {
            logger.warn("反馈记录不存在，记录ID：{}", feedbackId);
            return false;
        }
        
        // 更新反馈状态
        record.setStatus(status);
        record.setUpdated_at(new Date());
        
        return feedbackRecordsMapper.updateFeedbackRecord(record) > 0;
    }
    
    @Override
    public boolean deleteFeedbackRecordByCounselor(Long feedbackId, Long counselorId) {
        logger.info("辅导员删除反馈记录，反馈ID：{}，辅导员ID：{}", feedbackId, counselorId);
        // 验证记录是否存在并且属于辅导员负责的学生
        int count = feedbackRecordsMapper.countFeedbackRecordByIdAndCounselorId(feedbackId, counselorId);
        if (count == 0) {
            logger.warn("反馈记录不存在或不属于辅导员负责的学生，记录ID：{}，辅导员ID：{}", feedbackId, counselorId);
            return false;
        }
        
        return feedbackRecordsMapper.deleteFeedbackRecord(feedbackId) > 0;
    }
    
    @Override
    public Map<String, Object> getFeedbackRecordDetailByCounselor(Long feedbackId, Long counselorId) {
        logger.info("获取反馈详情（辅导员视角），反馈ID：{}，辅导员ID：{}", feedbackId, counselorId);
        // 验证记录是否存在并且属于辅导员负责的学生
        int count = feedbackRecordsMapper.countFeedbackRecordByIdAndCounselorId(feedbackId, counselorId);
        if (count == 0) {
            logger.warn("反馈记录不存在或不属于辅导员负责的学生，记录ID：{}，辅导员ID：{}", feedbackId, counselorId);
            return null;
        }
        
        return feedbackRecordsMapper.selectFeedbackRecordDetailById(feedbackId);
    }
}




