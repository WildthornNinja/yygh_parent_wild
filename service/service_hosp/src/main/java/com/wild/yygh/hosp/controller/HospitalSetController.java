package com.wild.yygh.hosp.controller;

import com.wild.yygh.common.R;
import com.wild.yygh.hosp.service.HospitalSetService;
import com.wild.yygh.model.hosp.HospitalSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //医院设置接口
@RequestMapping("/admin/hosp/hospitalSet")
@Api(tags = "首页模块")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 查询所有医院设置
     */
    @ApiOperation(value = "医院设置列表")
    @GetMapping("/findAll")
    public R findAll() {
        List<HospitalSet> hospitalSetList = hospitalSetService.list();
        return R.ok().data("hospitalSetList",hospitalSetList);
    }
    /**
     * 删除医院设置
     */
    @ApiOperation("通过id删除医院信息")
    @DeleteMapping("/delete/{id}")
    public R deleteHospSetById(@PathVariable Long id){
        boolean result = hospitalSetService.removeById(id);
        if(result){
            return R.ok();
        }else{
            return R.error();
        }
    }
    /**
     * 新增医院设置
     */
    @ApiOperation("新增医院设置")
    @PostMapping("/add")
    public R addHospSet(@ApiParam(name="hospitalSet",value = "医院设置对象",required = true)
            @RequestBody HospitalSet hospitalSet){
        //参数判空
        if(hospitalSet != null){
            boolean result = hospitalSetService.save(hospitalSet);
            if(result){
                return R.ok();
            }else{
                return R.error();
            }
        }else {
            return R.error();
        }
    }
    /**
     * 修改医院设置
     */
    //1.数据回显
    @ApiOperation("通过ID查询医院设置")
    @GetMapping("/getById/{id}")
    public R getHospById(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return R.ok().data("hospitalSet",hospitalSet);
    }
    //2.进行更新操作
    @ApiOperation("修改医院设置")
    @PutMapping("/update")
    public R updateHospSet(@RequestBody HospitalSet hospitalSet){
        //参数判空
        if(hospitalSet != null){
            boolean result = hospitalSetService.updateById(hospitalSet);
            if(result){
                return R.ok();
            }else{
                return R.error();
            }
        }else {
            return R.error();
        }
    }

}
