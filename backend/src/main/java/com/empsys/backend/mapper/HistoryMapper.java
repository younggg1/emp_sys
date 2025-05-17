package com.empsys.backend.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface HistoryMapper {

    @Select("SELECT er.record_id AS id, u.username AS student_id, s.name, s.major, er.company_nature, er.company AS company_name, er.position, er.salary, er.entry_date, er.region " +
            "FROM employment_records er " +
            "LEFT JOIN students s ON er.student_id = s.student_id " +
            "LEFT JOIN users u ON s.student_id = u.user_id " +
            "ORDER BY er.entry_date DESC")
    List<Map<String, Object>> findAll();

    // 删除
    @Delete("DELETE FROM employment_records WHERE record_id = #{recordId}")
    int deleteById(Long recordId);
}