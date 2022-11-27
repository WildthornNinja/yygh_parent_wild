package com.wild.yygh.cmn.test;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * ExcelListener监听器监听读取excel
 */
public class ExcelListener extends AnalysisEventListener<Student> {
    //一行一行去读取excle内容
    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        System.out.println("student = " + student);
    }
    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }
    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
