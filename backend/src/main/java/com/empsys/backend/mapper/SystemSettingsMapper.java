package com.empsys.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.empsys.backend.entity.SystemSettings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemSettingsMapper extends BaseMapper<SystemSettings> {
    
    /**
     * 获取学校系统设置
     * @param schoolId 学校ID
     * @return 系统设置对象
     */
    SystemSettings getSettingsBySchool(@Param("schoolId") Long schoolId);
} 
