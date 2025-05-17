package com.empsys.backend.service;

import java.util.List;
import java.util.Map;

public interface HistoryService {

    /**
     * 查询所有
     * @return
     */

    List<Map<String, Object>> findAll();


    /**
     * 删除
     * @param recordId
     * @return
     */
    boolean deleteById(Long recordId);
}