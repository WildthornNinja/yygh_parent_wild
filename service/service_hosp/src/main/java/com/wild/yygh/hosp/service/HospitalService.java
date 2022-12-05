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

    /**
     * 更新医院上线状态
     *
     * @param id
     * @param status
     */
    void updateStatus(String id, Integer status);

    /**
     * 获取医院详情
     *
     * @param id
     * @return
     */
    Map<String, Object> show(String id);

    /**
     * 获取医院名称
     *
     * @param hoscode
     * @return
     */
    String getHospName(String hoscode);
}
