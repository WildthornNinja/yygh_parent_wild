package com.wild.yygh.hosp.controller;

import com.wild.yygh.common.Result;
import com.wild.yygh.hosp.service.HospitalService;
import com.wild.yygh.hosp.utils.HttpRequestHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation(value = "上传医院数据")
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request){
        //1获取并转化参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        //2 TODO 签名校验
        //3调用方法存储数据
        hospitalService.saveHospital(paramMap);
        //4返回结果
        return Result.ok();
    }

}
