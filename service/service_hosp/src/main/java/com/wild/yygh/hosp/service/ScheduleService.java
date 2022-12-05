package com.wild.yygh.hosp.service;

import com.wild.yygh.model.hosp.Schedule;
import com.wild.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {
    /**
     * 上传排班
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);
    /**
     * 带条件带分页查询排版信息
     * @param page
     * @param limit
     * @param scheduleQueryVo
     * @return
     */
    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    /**
     * 删除排班
     *
     * @param hoscode
     * @param hosScheduleId
     */
    void remove(String hoscode, String hosScheduleId);

    /**
     * 根据医院编号 和 科室编号 ，查询排班统计数据
     *
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode);
}
