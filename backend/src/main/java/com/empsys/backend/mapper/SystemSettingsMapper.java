package com.empsys.backend.mapper;

import com.empsys.backend.entity.SystemSettings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @description 针对表【system_settings】的数据库操作Mapper
* @Entity com.empsys.backend.entity.SystemSettings
*/
@Mapper
public interface SystemSettingsMapper extends BaseMapper<SystemSettings> {
    
    /**
     * 获取系统设置（默认第一条记录）
     * @return 系统设置
     */
    @Select("SELECT * FROM system_settings ORDER BY setting_id LIMIT 1")
    SystemSettings getSystemSettings();
} 