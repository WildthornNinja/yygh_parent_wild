package com.wild.yygh.hosp.service;

import com.wild.yygh.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {
    /**
     * 存储医院数据
     * @param paramMap
     */
    void saveHospital(Map<String, Object> paramMap);

    /**
     * [查询]获取医院信息
     * @param hoscode
     * @return
     */
    Hospital getHospByHoscode(String hoscode);

}
