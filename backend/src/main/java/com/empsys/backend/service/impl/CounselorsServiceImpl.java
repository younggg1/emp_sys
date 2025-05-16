package com.empsys.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.empsys.backend.entity.Counselors;
import com.empsys.backend.service.CounselorsService;
import com.empsys.backend.mapper.CounselorsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author Quagmire
* @description 针对表【counselors】的数据库操作Service实现
* @createDate 2025-05-16 19:04:51
*/
@Service
public class CounselorsServiceImpl extends ServiceImpl<CounselorsMapper, Counselors> implements CounselorsService{

    private static final Logger logger = LoggerFactory.getLogger(CounselorsServiceImpl.class);
    
    @Autowired
    private CounselorsMapper counselorsMapper;
    


}




