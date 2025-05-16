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

import java.util.List;

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
}




