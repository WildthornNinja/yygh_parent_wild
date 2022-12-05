package com.wild.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wild.yygh.common.exception.YyghException;
import com.wild.yygh.hosp.repository.ScheduleRepository;
import com.wild.yygh.hosp.service.DepartmentService;
import com.wild.yygh.hosp.service.HospitalService;
import com.wild.yygh.hosp.service.ScheduleService;
import com.wild.yygh.model.hosp.Department;
import com.wild.yygh.model.hosp.Schedule;
import com.wild.yygh.vo.hosp.BookingScheduleRuleVo;
import com.wild.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;

    /**
     * 上传排班
     *
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

    /**
     * 删除排班
     * @param hoscode
     * @param hosScheduleId
     */
    @Override
    public void remove(String hoscode, String hosScheduleId) {
        //先查询后删除
        //1.根据 hoscode、hosScheduleId
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule == null) {
            throw new YyghException(20001, "排班信息有误");
        }
        //2.根据id删除科室
        scheduleRepository.deleteById(schedule.getId());
    }

    /**
     * 根据医院编号 和 科室编号 ，查询排班统计数据
     *
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    @Override
    public Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode) {
        //1.定义最终返回对象
        Map<String, Object> result = new HashMap<>();
        //2.带条件带分页聚合查询(返回List集合)
        //2.1创建查询条件对象
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        //2.2创建聚合查询对象
        //拼MongoDB聚合查询语句
        Aggregation aggregation = Aggregation.newAggregation(
                //2.2.1设置筛选条件，缩小数据范围
                Aggregation.match(criteria),
                //2.2.2设置分组聚合信息
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                //2.2.3 排序
                Aggregation.sort(Sort.Direction.ASC, "workDate"),
                //2.2.4 分页
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        //2.3进行聚合查询
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.
                aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggregate.getMappedResults();
        //3.获取总记录数total【带条件聚合查询(返回total)
        //3.1创建聚合查询对象
        Aggregation aggTotal = Aggregation.newAggregation(
                //3.1.1设置筛选条件
                Aggregation.match(criteria),
                //3.1.2设置分组信息
                Aggregation.group("workDate")
        );
        //3.2进行聚合查询
        AggregationResults<BookingScheduleRuleVo> aggregateTotal = mongoTemplate.aggregate(aggTotal, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> totalList = aggregateTotal.getMappedResults();
        //3.3获取长度
        int total = totalList.size();
        //4.判断星期天数，周几(借助工具)
        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }
        //5.封装数据
        result.put("bookingScheduleRuleList", bookingScheduleRuleVoList);
        result.put("total", total);
        //6.补全返回前端页面的数据
        //获取医院名称
        String hosName = hospitalService.getHospName(hoscode);
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hosName);
        result.put("baseMap", baseMap);
        return result;
    }

    /**
     * 根据医院编号 、科室编号和工作日期，查询排班详细信息
     *
     * @param hoscode
     * @param depcode
     * @param workDate
     * @return
     */
    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
        //1.根据参数查询排班集合
        //创建返回结果集
        List<Schedule> list = scheduleRepository.getByHoscodeAndDepcodeAndWorkDate(
                hoscode, depcode, new DateTime(workDate).toDate()
        );
        //2.翻译字段
        list.forEach(item -> {
            this.packageSchedule(item);
        });
        return list;

    }

    /**
     * 字段翻译
     *
     * @param schedule
     * @return
     */
    private Schedule packageSchedule(Schedule schedule) {
        //设置医院名称
        schedule.getParam().put("hosname", hospitalService.getHospName(schedule.getHoscode()));
        //设置科室名称
        schedule.getParam().put("depname",
                departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        //设置日期对应星期
        schedule.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(schedule.getWorkDate())));
        return schedule;
    }


    /**
     * 根据日期获取周几数据
     *
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }
}
