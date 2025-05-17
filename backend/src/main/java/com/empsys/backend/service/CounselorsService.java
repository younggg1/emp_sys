package com.empsys.backend.service;

import com.empsys.backend.entity.Counselors;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
* @author Quagmire
* @description 针对表【counselors】的数据库操作Service
* @createDate 2025-05-16 19:04:51
*/
public interface CounselorsService extends IService<Counselors> {

    List<Counselors> getAllCounselors();

    /**
     * 更新辅导员权限
     * @param counselor_id 辅导员ID
     * @param permission 权限值（Y-可删除，N-不可删除）
     * @return 是否更新成功
     */
    boolean updatePermission(Long counselor_id, String permission);



    /**
     * 获取辅导员完整信息列表（包含工号）
     * @return 辅导员列表
     */
    List<Map<String, Object>> getCounselorListWithUserInfo();
}
