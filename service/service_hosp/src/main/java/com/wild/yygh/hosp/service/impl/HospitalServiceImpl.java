package com.wild.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wild.yygh.hosp.repository.HospitalRepository;
import com.wild.yygh.hosp.service.HospitalService;
import com.wild.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService{
    @Autowired
    private HospitalRepository hospitalRepository;

    /**
     * 存储医院数据
     * @param paramMap
     */
    @Override
    public void saveHospital(Map<String, Object> paramMap) {
        //1.参数对象转型   map => hospital
        String paramJsonString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(paramJsonString, Hospital.class);
        //2. 先查询后保存 使用hoscode 查询医院信息
        Hospital targeHospital = hospitalRepository.getByHoscode(hospital.getHoscode());
        if(targeHospital!=null){
            //3.若存在医院信息 则更新 ，
            hospital.setId(targeHospital.getId());
            hospital.setCreateTime(targeHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(targeHospital.getIsDeleted());
            hospital.setStatus(targeHospital.getStatus());
            hospitalRepository.save(hospital);
        }else{
            //4.若不存在则新增
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospital.setStatus(0);
            hospitalRepository.save(hospital);
        }
    }

    /**
     * [查询]获取医院信息
     * @param hoscode
     * @return
     */
    @Override
    public Hospital getHospByHoscode(String hoscode) {
        //通过hoscode字段 查询医院信息
        Hospital hospital = hospitalRepository.getByHoscode(hoscode);
        return hospital;
    }
}
