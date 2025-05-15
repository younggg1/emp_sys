package com.empsys.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.empsys.backend.entity.School;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SchoolMapper extends BaseMapper<School> {
    List<School> getAllSchools();
} 
