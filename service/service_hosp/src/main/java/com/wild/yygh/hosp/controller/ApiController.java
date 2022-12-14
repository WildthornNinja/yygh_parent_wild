package com.wild.yygh.hosp.controller;

import com.wild.yygh.common.Result;
import com.wild.yygh.common.exception.YyghException;
import com.wild.yygh.hosp.service.DepartmentService;
import com.wild.yygh.hosp.service.HospitalService;
import com.wild.yygh.hosp.service.HospitalSetService;
import com.wild.yygh.hosp.service.ScheduleService;
import com.wild.yygh.hosp.utils.HttpRequestHelper;
import com.wild.yygh.hosp.utils.MD5;
import com.wild.yygh.model.hosp.Department;
import com.wild.yygh.model.hosp.Hospital;
import com.wild.yygh.model.hosp.Schedule;
import com.wild.yygh.vo.acl.RoleQueryVo;
import com.wild.yygh.vo.hosp.DepartmentQueryVo;
import com.wild.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "上传医院数据")
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request){
        //1.获取并转化参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        //2.签名校验
            //2.1 获取医院签名
        String hoscode = (String) paramMap.get("hoscode");
        String sign = (String) paramMap.get("sign");
        //参数校验
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(20001,"失败");
        }
        //2.4 签名校验
        checkSign(hoscode,sign);
        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoData = (String)paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);

        //3.调用方法存储数据
        hospitalService.saveHospital(paramMap);
        //4.返回结果
        return Result.ok();
    }
    @ApiOperation(value = "获取医院信息")
    @PostMapping("/hospital/show")
    public Result hospital(HttpServletRequest request) {
        //1.获取并转化参数
        Map<String,Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //2.参数校验
        String hoscode = (String)paramMap.get("hoscode");
        String sign = (String) paramMap.get("sign");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(20001,"失败");
        }
        //3.签名校验
        checkSign(hoscode,sign);
        //4.调用接口查询信息
        Hospital hospital = hospitalService.getHospByHoscode(hoscode);
        return Result.ok(hospital);

    }
    @ApiOperation(value = "上传科室")
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        //1.获取并转化参数
        Map<String,Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //2.参数校验
        String hoscode = (String)paramMap.get("hoscode");
        String sign = (String) paramMap.get("sign");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(20001,"失败");
        }
        //3.签名校验
        checkSign(hoscode,sign);
        //4.调用接口方法
        departmentService.save(paramMap);
        return Result.ok();

    }
    @ApiOperation(value = "带条件带分页查询科室信息")
    @PostMapping("/department/list")
    public Result department(HttpServletRequest request) {
        //1.获取并转化参数
        Map<String,Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //2.参数校验
        String hoscode = (String)paramMap.get("hoscode");
        String sign = (String) paramMap.get("sign");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(20001,"失败");
        }
        //3.签名校验
        checkSign(hoscode,sign);
        //分页参数验空 封装查询条件
        int page = StringUtils.isEmpty((String)paramMap.get("page"))?1:
                Integer.parseInt((String)paramMap.get("page"));
        //判断每页记录数limit
        int limit = StringUtils.isEmpty((String)paramMap.get("limit"))?10:
                Integer.parseInt((String)paramMap.get("limit"));
        //数据封装为 Vo对象[查询条件对象]
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //4.调用接口方法
        Page<Department> pageModel = departmentService.selectPage(page,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }
    @ApiOperation(value = "删除科室")
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        //1.获取并转化参数
        Map<String,Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //2.参数校验
        String hoscode = (String)paramMap.get("hoscode");
        String sign = (String) paramMap.get("sign");
        String depcode = (String) paramMap.get("depcode");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(20001,"失败");
        }
        //3.签名校验
        checkSign(hoscode,sign);
        //4.调用接口方法
        departmentService.remove(hoscode,depcode);
        return Result.ok();

    }
    @ApiOperation(value = "上传排班")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        //1.获取并转化参数
        Map<String,Object> paramMap =HttpRequestHelper.switchMap(request.getParameterMap());
        //2.参数校验
        String hoscode = (String)paramMap.get("hoscode");
        String sign = (String) paramMap.get("sign");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(20001,"失败");
        }
        //3.签名校验
        checkSign(hoscode,sign);
        //4.调用接口方法
        scheduleService.save(paramMap);
        return Result.ok();
    }
    @ApiOperation(value = "带条件带分页查询排版信息")
    @PostMapping("/schedule/list")
    public Result schedule(HttpServletRequest request) {
        //1.获取并转化参数
        Map<String,Object> paramMap =HttpRequestHelper.switchMap(request.getParameterMap());
        //2.参数校验
        String hoscode = (String)paramMap.get("hoscode");
        String sign = (String) paramMap.get("sign");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(20001,"失败");
        }
        //3.签名校验
        checkSign(hoscode,sign);
        //分页参数验空 封装查询条件

        int page = StringUtils.isEmpty((String)paramMap.get("page"))?1:
                Integer.parseInt((String)paramMap.get("page"));
        //判断每页记录数limit
        int limit = StringUtils.isEmpty((String)paramMap.get("limit"))?10:
                Integer.parseInt((String)paramMap.get("limit"));
        //数据封装为 Vo对象[查询条件对象]
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        //4.调用接口方法
        Page<Schedule> pageModel = scheduleService.selectPage(page,limit,scheduleQueryVo);
        return Result.ok(pageModel);
    }
    /**
     * 抽取公共签名校验
     */
    public void checkSign(String hoscode,String sign){
        //调用接口获取自己本地签名
        String hospSign = hospitalSetService.getSignKey(hoscode);
        //本地签名使用MD5加密
        String hospSignMD5 = MD5.encrypt(hospSign);
        //签名校验
        if(!hospSignMD5.equals(sign)){
            throw new YyghException(20001,"签名校验失败");
        }
    }
    @ApiOperation(value = "删除排班")
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        //1.获取并转化参数
        Map<String,Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //2.参数校验
        String hoscode = (String)paramMap.get("hoscode");
        String sign = (String) paramMap.get("sign");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(20001,"失败");
        }
        //3.签名校验
        checkSign(hoscode,sign);
        //4.调用接口方法
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }

}
