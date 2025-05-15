package com.empsys.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.empsys.backend.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    
    /**
     * 获取用户权限
     * @param userId 用户ID
     * @return 权限对象
     */
    Permission getPermissionByUserId(@Param("userId") Long userId);
} 
