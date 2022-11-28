package com.wild.yygh.hosp.service;

import java.util.Map;

public interface ScheduleService {
    /**
     * 上传排班
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);
}
