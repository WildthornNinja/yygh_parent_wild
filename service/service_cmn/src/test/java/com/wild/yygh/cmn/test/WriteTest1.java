package com.wild.yygh.cmn.test;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试EasyExcl实现写操作
 *  写法1
 */
public class WriteTest1 {

    public static void main(String[] args) {
        /**
         * 写法1
         */
        String fileName = "D:\\test1.xlsx";
        //这里需要指定写用哪个class类去写，然后写到第一个sheet，名字为模板然后文件流会【自动关闭】
        // 如果这里想使用03则传入excelType参数即可
        EasyExcel.write(fileName,Student.class).sheet("学员信息-方法1")
                .doWrite(data());

    }

    //循环设置要添加的数据，最终封装到list集合中
    private static List<Student> data() {
        List<Student> list = new ArrayList<Student>();
        for (int i = 0; i < 10; i++) {
            Student data = new Student();
            data.setSno(i);
            data.setSname("wild【方法一】"+i);
            list.add(data);
        }
        return list;
    }
}
