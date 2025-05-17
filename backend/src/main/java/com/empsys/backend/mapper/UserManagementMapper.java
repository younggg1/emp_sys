package com.empsys.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.empsys.backend.entity.Counselors;
import com.empsys.backend.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 用户管理数据访问层
 */
@Mapper
public interface UserManagementMapper extends BaseMapper<Users> {
    
    /**
     * 获取用户列表
     * @param offset 偏移量
     * @param size 每页大小
     * @param role 角色
     * @param keyword 关键词
     * @return 用户列表
     */
    List<Users> selectUserList(@Param("offset") long offset, 
                             @Param("size") long size,
                             @Param("role") String role,
                             @Param("keyword") String keyword);
    
    /**
     * 获取用户总数
     * @param role 角色
     * @param keyword 关键词
     * @return 用户总数
     */
    long selectUserCount(@Param("role") String role,
                        @Param("keyword") String keyword);

    /**
     * 添加辅导员
     * @param counselor 辅导员
     * @return 添加结果
     */
    int insertCounselor(Counselors counselor);

    /**
     * 获取辅导员列表
     * @return 辅导员列表
     */
    List<Map<String, Object>> selectCounselorList();


    /**
     * 更新辅导员
     * @param counselor 辅导员信息
     * @return 更新结果
     */
    int updateCounselor(Counselors counselor);

    /**
     * 删除辅导员
     * @param id 辅导员ID
     * @return 删除结果
     */
    int deleteCounselor(@Param("id") Long id);
} 