package com.wild.yygh.cmn.test;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Student {
    //设置excl表头名称
    @ExcelProperty("学生编号")
    private int sno;

    //设置excl表头名称
    @ExcelProperty("学生姓名")
    private String sname;
}
