package com.wild.yygh.hosp.repository;

import com.wild.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    /**
     * 通过 hoscode、depcode俩字段查询department对象
     *
     * @param hoscode
     * @param depcode
     * @return
     */
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

    /**
     * 根据hoscode查询所有科室数据集合
     *
     * @param hoscode
     * @return
     */
    List<Department> getByHoscode(String hoscode);
}
