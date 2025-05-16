package com.empsys.backend.mapper;

import com.empsys.backend.entity.FeedbackRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Quagmire
* @description 针对表【feedback_records】的数据库操作Mapper
* @createDate 2025-05-16 18:10:11
* @Entity com.empsys.backend.entity.FeedbackRecords
*/
@Mapper
public interface FeedbackRecordsMapper extends BaseMapper<FeedbackRecords> {

    /**
     * 获取学生的反馈列表
     * @param studentId 学生ID
     * @return 反馈列表
     */
    List<FeedbackRecords> selectFeedbackRecordsByStudentId(@Param("studentId") Long studentId);

    /**
     * 分页获取学生的反馈列表
     * @param page 分页参数
     * @param studentId 学生ID
     * @return 分页后的反馈列表
     */
    IPage<FeedbackRecords> selectFeedbackRecordsPageByStudentId(Page<FeedbackRecords> page, @Param("studentId") Long studentId);

    /**
     * 添加反馈
     * @param record 反馈记录
     * @return 影响行数
     */
    int insertFeedbackRecord(FeedbackRecords record);

    /**
     * 更新反馈
     * @param record 反馈记录
     * @return 影响行数
     */
    int updateFeedbackRecord(FeedbackRecords record);

    /**
     * 删除反馈
     * @param feedbackId 反馈ID
     * @return 影响行数
     */
    int deleteFeedbackRecord(@Param("feedbackId") Long feedbackId);

    /**
     * 根据ID获取反馈
     * @param feedbackId 反馈ID
     * @return 反馈信息
     */
    FeedbackRecords selectFeedbackRecordById(@Param("feedbackId") Long feedbackId);

    /**
     * 验证反馈是否属于指定学生
     * @param feedbackId 反馈ID
     * @param studentId 学生ID
     * @return 反馈记录数（应为0或1）
     */
    int countFeedbackRecordByIdAndStudentId(@Param("feedbackId") Long feedbackId, @Param("studentId") Long studentId);
    
    /**
     * 获取辅导员负责的学生反馈列表
     * @param counselorId 辅导员ID
     * @return 反馈列表
     */
    List<Map<String, Object>> selectFeedbackRecordsByCounselorId(@Param("counselorId") Long counselorId);
    
    /**
     * 分页获取辅导员负责的学生反馈列表
     * @param page 分页参数
     * @param counselorId 辅导员ID
     * @return 分页后的反馈列表
     */
    IPage<Map<String, Object>> selectFeedbackRecordsPageByCounselorId(Page<Map<String, Object>> page, @Param("counselorId") Long counselorId);
    
    /**
     * 验证反馈是否属于辅导员负责的学生
     * @param feedbackId 反馈ID
     * @param counselorId 辅导员ID
     * @return 反馈记录数（应为0或1）
     */
    int countFeedbackRecordByIdAndCounselorId(@Param("feedbackId") Long feedbackId, @Param("counselorId") Long counselorId);
    
    /**
     * 获取反馈详情（包含学生信息）
     * @param feedbackId 反馈ID
     * @return 反馈详情
     */
    Map<String, Object> selectFeedbackRecordDetailById(@Param("feedbackId") Long feedbackId);
}




