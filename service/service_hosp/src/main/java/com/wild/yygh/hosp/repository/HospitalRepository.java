package com.wild.yygh.hosp.repository;

import com.wild.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    /**
     * 获取医院唯一标识
     * @param hoscode
     * @return
     */
    Hospital getByHoscode(String hoscode);
}
