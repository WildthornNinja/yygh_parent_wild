package com.wild.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wild.yygh.hosp.repository.ScheduleRepository;
import com.wild.yygh.hosp.service.ScheduleService;
import com.wild.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * 上传排班
     * @param paramMap
     */
    @Override
    public void save(Map<String, Object> paramMap) {
        //1.参数对象转型   map => Schedule
        String paramJsonString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(paramJsonString, Schedule.class);
        //2.查询MongoDB，确认是否有科室信息【hoscode、hosScheduleId】
        Schedule targetSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());

        if(targetSchedule !=null){
            //3.有科室数据，进行更新
            schedule.setId(targetSchedule.getId());
            schedule.setCreateTime(targetSchedule.getCreateTime());
            schedule.setUpdateTime(new Date());
            scheduleRepository.save(schedule);
        }else {
            //4.若无科室数据，进行新增
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        }
    }
}
