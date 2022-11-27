package com.wild.yygh.cmn.test;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Student {
    //设置excl表头名称
    //@ExcelProperty("学生编号")
    //设置列对应的属性
    @ExcelProperty(value = "学生编号",index = 0)
    private int sno;

    //设置excl表头名称
    //@ExcelProperty("学生姓名")
    //设置列对应的属性
    @ExcelProperty(value = "学生姓名",index = 1)
    private String sname;
}
