package com.empsys.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.entity.Users;

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
} 