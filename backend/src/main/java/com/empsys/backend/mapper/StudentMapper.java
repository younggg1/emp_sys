package com.empsys.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.empsys.backend.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    
    /**
     * 获取学生详细信息（包含辅导员姓名）
     * @param studentId 学生ID
     * @return 学生信息
     */
    Student getStudentInfo(@Param("studentId") Long studentId);
    
    /**
     * 获取指定辅导员管理的学生列表
     * @param counselorId 辅导员ID
     * @return 学生列表
     */
    List<Student> getStudentsByCounselor(@Param("counselorId") Long counselorId);
    
    /**
     * 获取指定学校的所有学生
     * @param page 分页参数
     * @param schoolId 学校ID
     * @return 学生分页数据
     */
    IPage<Student> getStudentsBySchool(Page<Student> page, @Param("schoolId") Long schoolId);
} 
