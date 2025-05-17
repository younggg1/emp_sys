package com.empsys.backend.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.entity.Counselors;
import com.empsys.backend.entity.Users;
import com.empsys.backend.mapper.UserManagementMapper;
import com.empsys.backend.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户管理服务实现类
 */
@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private UserManagementMapper userManagementMapper;

    @Override
    public IPage<Users> getUserList(Page<Users> page, String role, String keyword) {
        // 计算偏移量
        long offset = (page.getCurrent() - 1) * page.getSize();
        
        System.out.println("查询参数：page=" + page.getCurrent() + ", size=" + page.getSize() + ", role=" + role + ", keyword=" + keyword);
        System.out.println("计算偏移量：offset=" + offset);
        
        // 获取总数
        long total = userManagementMapper.selectUserCount(role, keyword);
        System.out.println("查询总数：total=" + total);
        page.setTotal(total);
        
        // 获取分页数据
        List<Users> records = userManagementMapper.selectUserList(offset, page.getSize(), role, keyword);
        System.out.println("查询结果：");
        for (Users user : records) {
            System.out.println("用户ID：" + user.getUser_id());
            System.out.println("用户名：" + user.getUsername());
            System.out.println("角色：" + user.getRole());
            System.out.println("创建时间：" + user.getCreated_at());
            System.out.println("更新时间：" + user.getUpdated_at());
            System.out.println("-------------------");
        }
        page.setRecords(records);
        
        return page;
    }



    @Override
    @Transactional
    public boolean addCounselor(Users user, String name) {
        // 1. 检查用户名是否已存在
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, user.getUsername());
        if (userManagementMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 设置用户角色为辅导员
        user.setRole("counselor");

        // 3. 保存用户信息
        boolean userSaved = userManagementMapper.insert(user) > 0;
        if (!userSaved) {
            throw new RuntimeException("保存用户信息失败");
        }

        // 4. 创建辅导员信息
        Counselors counselor = new Counselors();
        counselor.setCounselor_id(user.getUser_id());
        counselor.setName(name);

        // 5. 保存辅导员信息
        boolean counselorSaved = userManagementMapper.insertCounselor(counselor) > 0;
        if (!counselorSaved) {
            throw new RuntimeException("保存辅导员信息失败");
        }

        return true;
    }

} 