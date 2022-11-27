package com.wild.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.wild.yygh.cmn.mapper.DictMapper;
import com.wild.yygh.model.cmn.Dict;
import com.wild.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听器读取文件内容，并写入数据库
 */
@Component
public class DictListener extends AnalysisEventListener<DictEeVo> {
    //自动注入
    @Autowired
    private DictMapper dictMapper;
    /**
     *知识回顾: 手动注入 ，利用有参构造器+ 属性 实现
     * new DictListener (DictMapper dictMapper)
     * private DictMapper dictMapper
     *
     */

    //一行一行读取
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        //【数据转型】 将文件中读取的 DictEeVo对象 ，转型成数据库实体Dict对象
        Dict dict = new Dict();
        //调用BeanUtils工具类进行对象复制
        BeanUtils.copyProperties(dictEeVo,dict);
        //设置删除默认状态
        dict.setIsDeleted(0);
        //调用方法添加数据库
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

}
