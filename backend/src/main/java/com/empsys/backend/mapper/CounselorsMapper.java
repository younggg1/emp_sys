package com.empsys.backend.mapper;

import com.empsys.backend.entity.Counselors;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Date;

/**
* @author Quagmire
* @description 针对表【counselors】的数据库操作Mapper
* @createDate 2025-05-16 19:04:51
* @Entity com.empsys.backend.entity.Counselors
*/
@Mapper
public interface CounselorsMapper extends BaseMapper<Counselors> {
    
    List<Counselors> getAllCounselors();

    /**
     * 获取辅导员完整信息列表（包含工号）
     * @return 辅导员列表
     */
    List<Map<String, Object>> getCounselorListWithUserInfo();

    /**
     * 更新辅导员权限
     * @param counselor_id 辅导员ID
     * @param permission 权限值（yes/no）
     * @param updated_at 更新时间
     * @return 影响的行数
     */
    int updatePermission(@Param("counselor_id") Long counselor_id, 
                        @Param("permission") String permission,
                        @Param("updated_at") Date updated_at);
}




