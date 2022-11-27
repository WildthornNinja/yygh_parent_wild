package com.wild.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wild.yygh.model.cmn.Dict;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    /***
     * 根据数据id查询子数据列表
     * @param id
     * @return
     */
    List<Dict> findChildData(Long id);

    /**
     * 导出数据
     * 从Response对象中获取数据，具体在业务层实现
     */
    void exportData(HttpServletResponse response);
}
