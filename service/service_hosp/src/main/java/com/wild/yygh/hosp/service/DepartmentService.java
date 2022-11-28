package com.wild.yygh.hosp.service;

import com.wild.yygh.model.hosp.Department;
import com.wild.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DepartmentService {

    /**
     * [新增]上传科室
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 带条件带分页查询科室信息
     * @param page
     * @param limit
     * @param departmentQueryVo
     * @return
     */
    Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo);

    /**
     * 删除科室
     * @param hoscode
     * @param depcode
     */
    void remove(String hoscode, String depcode);
}
