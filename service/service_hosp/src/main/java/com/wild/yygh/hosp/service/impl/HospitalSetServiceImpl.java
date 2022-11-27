package com.wild.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wild.yygh.common.exception.YyghException;
import com.wild.yygh.hosp.mapper.HospitalSetMapper;
import com.wild.yygh.hosp.service.HospitalSetService;
import com.wild.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper,HospitalSet> implements HospitalSetService{
    @Autowired
    private HospitalSetMapper hospitalSetMapper;
    /**
     * 获取签名key
     * @param hoscode
     * @return
     */
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(hospitalSet!=null){
            return hospitalSet.getSignKey();
        }else {
            throw new YyghException(20001,"获取签名失败");
        }
    }
}

