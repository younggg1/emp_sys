package com.empsys.backend.service;

import com.empsys.backend.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Quagmire
* @description 针对表【users】的数据库操作Service
* @createDate 2025-05-16 16:43:43
*/
public interface UsersService extends IService<Users> {
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param role 角色
     * @return 用户信息
     */
    Users login(String username, String password, String role);
}
