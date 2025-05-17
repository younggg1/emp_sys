package com.empsys.backend.service.impl;

import com.empsys.backend.mapper.HistoryMapper;
import com.empsys.backend.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public List<Map<String, Object>> findAll() {
        return historyMapper.findAll();
    }

    @Override
    public boolean deleteById(Long recordId) {
        return historyMapper.deleteById(recordId) > 0;
    }
}