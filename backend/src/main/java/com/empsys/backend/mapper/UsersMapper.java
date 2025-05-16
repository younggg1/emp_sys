package com.empsys.backend.mapper;

import com.empsys.backend.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Quagmire
* @description 针对表【users】的数据库操作Mapper
* @createDate 2025-05-16 16:43:43
* @Entity com.empsys.backend.entity.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @param password 密码
     * @param role 角色
     * @return 用户信息
     */
    Users selectByUsernameAndPassword(@Param("username") String username, 
                                    @Param("password") String password, 
                                    @Param("role") String role);
}




