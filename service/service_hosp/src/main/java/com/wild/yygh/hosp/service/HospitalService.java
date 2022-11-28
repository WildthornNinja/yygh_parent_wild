package com.wild.yygh.hosp.service;

import com.wild.yygh.model.hosp.Hospital;
import com.wild.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {
    /**
     * 存储医院数据
     * @param paramMap
     */
    void saveHospital(Map<String, Object> paramMap);

    /**
     * [查询]获取医院信息
     *
     * @param hoscode
     * @return
     */
    Hospital getHospByHoscode(String hoscode);

    /**
     * 带条件带分页查询医院列表
     *
     * @param page
     * @param limit
     * @param hospitalQueryVo
     * @return
     */
    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
}
