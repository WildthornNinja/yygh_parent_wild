package com.wild.yygh.cmn.controller;

import com.wild.yygh.cmn.service.DictService;
import com.wild.yygh.common.R;
import com.wild.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {
    @Autowired
    private DictService dictService;
    /**
     * 根据数据id查询子数据列表
     */
    @ApiOperation("根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public R findChildData(@PathVariable Long id){
        //调用service接口方法，查询该id数据的子数据列表
        List<Dict> dictList = dictService.findChildData(id);

        return R.ok().data("dictList",dictList);
    }


}
