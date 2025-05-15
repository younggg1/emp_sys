package com.empsys.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.empsys.backend.entity.Counselor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CounselorMapper extends BaseMapper<Counselor> {
    
    /**
     * 获取指定学校的所有辅导员
     * @param schoolId 学校ID
     * @return 辅导员列表
     */
    List<Counselor> getCounselorsBySchool(@Param("schoolId") Long schoolId);
} 
