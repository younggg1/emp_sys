package com.empsys.backend.controller;

import com.empsys.backend.config.Result;
import com.empsys.backend.entity.Users;
import com.empsys.backend.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UsersService usersService;

    /**
     * 用户登录接口 - JSON提交方式
     * @param params 包含用户名、密码、角色的Map
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String role = params.get("role");
        
        // 参数校验
        if (username == null || password == null) {
            return Result.error("用户名或密码不能为空");
        }
        
        // 调用登录服务
        Users user = usersService.login(username, password, role);
        
        // 用户不存在或密码错误
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        // 构建返回结果
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", user.getUser_id());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        
        return Result.success(data);
    }
}
