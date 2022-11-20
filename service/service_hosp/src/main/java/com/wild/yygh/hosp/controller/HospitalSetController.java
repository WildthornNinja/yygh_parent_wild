package com.wild.yygh.hosp.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.wild.yygh.hosp.service.HospitalSetService;
import com.wild.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //医院设置接口
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 查询所有医院设置
     */
    @GetMapping("/findAll")
    public List<HospitalSet> findAll() {
        List<HospitalSet> hospitalSetList = hospitalSetService.list();
        return hospitalSetList;
    }

}