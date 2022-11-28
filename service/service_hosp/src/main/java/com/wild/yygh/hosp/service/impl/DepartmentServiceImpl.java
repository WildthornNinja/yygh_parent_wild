package com.wild.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wild.yygh.hosp.repository.DepartmentRepository;
import com.wild.yygh.hosp.service.DepartmentService;
import com.wild.yygh.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * [新增]上传科室
     * @param paramMap
     */
    @Override
    public void save(Map<String, Object> paramMap) {
        //1.转化对象，将 map 对象转化成 department对象
        String paramJSONString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramJSONString, Department.class);
        //2.查询MongoDB，确认是否有科室信息【hoscode、depcode】
        Department targetDepartment =departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());
        if(targetDepartment!=null){
            //3.有科室数据，进行更新
            department.setId(targetDepartment.getId());
            department.setCreateTime(targetDepartment.getCreateTime());
            department.setUpdateTime(new Date());
            departmentRepository.save(department);

        }else{
            //4.若无科室数据，进行新增
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }
}
