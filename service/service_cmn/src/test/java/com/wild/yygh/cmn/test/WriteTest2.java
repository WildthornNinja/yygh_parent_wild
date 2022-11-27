package com.wild.yygh.cmn.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试EasyExcl实现写操作
 *  写法2
 */
public class WriteTest2 {
    public static void main(String[] args) {
        /**
         * 写法2
         */
        // 写法2，方法二需要手动关闭流
        String fileName ="D:\\test2.xlsx";
        // 这里需要指定写用哪个class去写
        ExcelWriter excelWriter = EasyExcel.write(fileName,Student.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("学员信息-方法2").build();
        //组合
        excelWriter.write(data(),writeSheet);
        // 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();


    }
    //循环设置要添加的数据，最终封装到list集合中
    private static List<Student> data() {
        List<Student> list = new ArrayList<Student>();
        for (int i = 0; i < 10; i++) {
            Student data = new Student();
            data.setSno(i);
            data.setSname("wild【方法二】"+i);
            list.add(data);
        }
        return list;
    }
}
