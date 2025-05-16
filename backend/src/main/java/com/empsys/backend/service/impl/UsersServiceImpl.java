package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.empsys.backend.entity.Users;
import com.empsys.backend.service.UsersService;
import com.empsys.backend.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Quagmire
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-05-16 16:43:43
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users login(String username, String password, String role) {
        // 使用自定义方法进行查询
        return usersMapper.selectByUsernameAndPassword(username, password, role);
    }
}




