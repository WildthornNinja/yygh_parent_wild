package com.wild.yygh.cmn.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@EnableTransactionManagement
@MapperScan("com.wild.yygh.cmn.mapper")
public class CmnConfig {
}
