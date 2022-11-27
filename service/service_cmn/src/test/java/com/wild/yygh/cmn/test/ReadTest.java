package com.wild.yygh.cmn.test;

import com.alibaba.excel.EasyExcel;

public class ReadTest {
    public static void main(String[] args) {
        String fileName1 = "D:\\test1.xlsx";
        EasyExcel.read(fileName1,Student.class,new ExcelListener()).sheet().doRead();
        String fileName2 = "D:\\test2.xlsx";
        EasyExcel.read(fileName2,Student.class,new ExcelListener()).sheet().doRead();
    }
}
