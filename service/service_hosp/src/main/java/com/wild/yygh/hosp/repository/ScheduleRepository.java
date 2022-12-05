package com.wild.yygh.hosp.repository;

import com.wild.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    /**
     * 通过 hoscode、hosScheduleId 查询Schedule排班对象
     *
     * @param hoscode
     * @param hosScheduleId
     * @return
     */
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    /**
     * 通过 hoscode、depcode workDate 查询Schedule排班对象
     *
     * @param hoscode
     * @param depcode
     * @param workDate
     * @return
     */
    List<Schedule> getByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date workDate);
}
