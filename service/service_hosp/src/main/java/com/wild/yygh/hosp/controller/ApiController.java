package com.wild.yygh.hosp.controller;

import com.wild.yygh.common.Result;
import com.wild.yygh.common.exception.YyghException;
import com.wild.yygh.hosp.service.HospitalService;
import com.wild.yygh.hosp.service.HospitalSetService;
import com.wild.yygh.hosp.utils.HttpRequestHelper;
import com.wild.yygh.hosp.utils.MD5;
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
    @Autowired
    private HospitalSetService hospitalSetService;

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
            //2.2 调用接口获取自己本地签名
        String hospSign = hospitalSetService.getSignKey(hoscode);
            //2.3本地签名使用MD5加密
        String hospSignMD5 = MD5.encrypt(hospSign);
        System.out.println("sign = " + sign);
        System.out.println("hospSignMD5 = " + hospSignMD5);
            //2.4 签名校验
        if(!hospSignMD5.equals(sign)){
            throw new YyghException(20001,"签名校验失败");
        }
        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoData = (String)paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);

        //3.调用方法存储数据
        hospitalService.saveHospital(paramMap);
        //4.返回结果
        return Result.ok();
    }

}
