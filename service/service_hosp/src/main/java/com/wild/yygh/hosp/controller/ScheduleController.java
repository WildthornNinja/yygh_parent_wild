package com.wild.yygh.hosp.controller;

import com.wild.yygh.common.R;
import com.wild.yygh.hosp.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "排班接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    /**
     * 根据医院编号 和 科室编号 ，查询排班统计数据
     *
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    @ApiOperation(value = "查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public R getScheduleRule(@PathVariable long page,
                             @PathVariable long limit,
                             @PathVariable String hoscode,
                             @PathVariable String depcode) {
        Map<String, Object> map
                = scheduleService.getScheduleRule(page, limit, hoscode, depcode);
        return R.ok().data(map);
    }
}
