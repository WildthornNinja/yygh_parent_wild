package com.wild.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wild.yygh.hosp.repository.ScheduleRepository;
import com.wild.yygh.hosp.service.ScheduleService;
import com.wild.yygh.model.hosp.Department;
import com.wild.yygh.model.hosp.Schedule;
import com.wild.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    /**
     * 带条件带分页查询排版信息
     * @param page
     * @param limit
     * @param scheduleQueryVo
     * @return
     */
    @Override
    public Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        //1.创建分页对象 Pageable
        //1.1创建排序对象 Sort
        Sort sort =Sort.by(Sort.Direction.DESC,"creatTime");
        //1.2创建分页对象 ,注意：第一天业 page为0
        Pageable pageable = PageRequest.of((page-1),limit,sort);
        //2.创建查询条件
        //2.1创建查询模板构造器
        ExampleMatcher matcher = ExampleMatcher.matching()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                //改变默认大小写忽略方式：忽略大小写
                .withIgnoreCase(true);
        //2.2创建查询条件
        Schedule schedule = new Schedule();
        //对象转换，复制
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        //2.3创建条件查询模板
        Example<Schedule> example = Example.of(schedule,matcher);
        //3.带条件带分页查询
        Page<Schedule> schedulePage = scheduleRepository.findAll(example,pageable);

        return schedulePage;

    }
}
