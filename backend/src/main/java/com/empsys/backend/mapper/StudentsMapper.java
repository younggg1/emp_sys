package com.empsys.backend.mapper;

import com.empsys.backend.entity.Students;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author Quagmire
* @description 针对表【students】的数据库操作Mapper
* @createDate 2025-05-16 17:12:31
* @Entity com.empsys.backend.entity.Students
*/
@Mapper
public interface StudentsMapper extends BaseMapper<Students> {

    /**
     * 根据用户ID查询学生信息
     * @param userId 用户ID
     * @return 学生信息（包括辅导员姓名）
     */
    Students selectStudentInfoByUserId(@Param("userId") Long userId);
    
    /**
     * 根据辅导员ID查询学生列表
     * @param counselorId 辅导员ID
     * @return 学生列表
     */
    List<Students> selectStudentsByCounselorId(@Param("counselorId") Long counselorId);


    /**
     * 添加学生
     * @param student 学生信息
     * @return 添加结果
     */
    int insertStudent(Students student);
}




