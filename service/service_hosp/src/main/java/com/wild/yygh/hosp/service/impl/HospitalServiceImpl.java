package com.wild.yygh.hosp.service.impl;

import com.wild.yygh.hosp.repository.HospitalRepository;
import com.wild.yygh.hosp.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalServiceImpl implements HospitalService{
    @Autowired
    private HospitalRepository hospitalRepository;

}
