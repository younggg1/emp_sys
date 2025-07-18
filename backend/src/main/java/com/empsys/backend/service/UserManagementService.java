package com.empsys.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.entity.Users;

import java.util.List;
import java.util.Map;

/**
 * 用户管理服务接口
 */
public interface UserManagementService {

    /**
     * 获取用户列表
     * @param page 分页参数
     * @param role 角色筛选
     * @param keyword 关键词搜索
     * @return 用户列表
     */
    IPage<Users> getUserList(Page<Users> page, String role, String keyword);


    /**
     * 添加辅导员
     * @param user 用户信息
     * @param name 咨询师名称
     * @return 是否添加成功
     */
    boolean addCounselor(Users user, String name);

    /**
     * 获取辅导员列表
     * @return 辅导员列表
     */
    List<Map<String, Object>> getCounselorList();

    /**
     * 添加学生
     * @param studentData 学生数据
     * @return 是否添加成功
     */
    boolean addStudent(Map<String, Object> studentData);

    /**
     * 更新用户
     * @param id 用户ID
     * @param userData 用户数据
     * @return 是否更新成功
     */
    boolean updateUser(Long id, Map<String, Object> userData);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(Long id);
} 