package com.wild.yygh.hosp.service.impl;

import com.wild.yygh.hosp.repository.DepartmentRepository;
import com.wild.yygh.hosp.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
}
