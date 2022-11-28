package com.wild.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wild.yygh.hosp.repository.DepartmentRepository;
import com.wild.yygh.hosp.service.DepartmentService;
import com.wild.yygh.model.hosp.Department;
import com.wild.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    /**
     * 带条件带分页查询科室信息
     * @param page
     * @param limit
     * @param departmentQueryVo
     * @return
     */
    @Override
    public Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        //1.创建分页对象 Pageable
            //1.1创建排序对象 Sort
        Sort sort =Sort.by(Sort.Direction.DESC,"creatTime");
            //1.2创建分页对象 ,注意：第一天业 page为0
        Pageable pageable = PageRequest.of((page-1),limit,sort);
        //2.创建查询条件
            //2.1创建查询模板构造器
        ExampleMatcher matcher = ExampleMatcher.matching()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                //改变默认大小写忽略方式：忽略大小写
                .withIgnoreCase(true);
            //2.2创建查询条件
        Department department = new Department();
        //对象转换，复制
        BeanUtils.copyProperties(departmentQueryVo,department);
            //2.3创建条件查询模板
        Example<Department> example = Example.of(department,matcher);
        //3.带条件带分页查询
        Page<Department> departmentPage = departmentRepository.findAll(example,pageable);

        return departmentPage;
    }
}
