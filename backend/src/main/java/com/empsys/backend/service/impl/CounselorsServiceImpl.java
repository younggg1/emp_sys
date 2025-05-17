package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.empsys.backend.entity.Counselors;
import com.empsys.backend.service.CounselorsService;
import com.empsys.backend.mapper.CounselorsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author Quagmire
* @description 针对表【counselors】的数据库操作Service实现
* @createDate 2025-05-16 19:04:51
*/
@Service
public class CounselorsServiceImpl extends ServiceImpl<CounselorsMapper, Counselors> implements CounselorsService {

    private static final Logger logger = LoggerFactory.getLogger(CounselorsServiceImpl.class);
    
    @Autowired
    private CounselorsMapper counselorsMapper;
    
    @Override
    public List<Counselors> getAllCounselors() {
        return baseMapper.getAllCounselors();
    }

    @Override
    public boolean updatePermission(Long counselor_id, String permission) {
        logger.info("开始更新辅导员权限，counselorId: {}, permission: {}", counselor_id, permission);
        
        Counselors counselor = getById(counselor_id);
        if (counselor == null) {
            logger.error("未找到ID为{}的辅导员", counselor_id);
            return false;
        }
        logger.info("找到辅导员信息: {}", counselor);
        
        Date now = new Date();
        int rows = counselorsMapper.updatePermission(counselor_id, permission, now);
        logger.info("更新权限结果，影响行数: {}", rows);
        
        return rows > 0;
    }



    @Override
    public List<Map<String, Object>> getCounselorListWithUserInfo() {
        return baseMapper.getCounselorListWithUserInfo();
    }
}




