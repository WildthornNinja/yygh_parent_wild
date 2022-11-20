package com.wild.yygh.hosp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@EnableTransactionManagement //开启事务管理
@MapperScan("com.wild.yygh.hosp.mapper")
public class HospConfig {
}
