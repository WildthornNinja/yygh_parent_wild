package com.wild.yygh.cmn.controller;

import com.sun.deploy.net.HttpResponse;
import com.wild.yygh.cmn.service.DictService;
import com.wild.yygh.common.R;
import com.wild.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    @GetMapping("/findChildData/{id}")
    public R findChildData(@PathVariable Long id){
        //调用service接口方法，查询该id数据的子数据列表
        List<Dict> dictList = dictService.findChildData(id);

        return R.ok().data("dictList",dictList);
    }
    /**
     * 导出数据
     * 从Response对象中获取数据，具体在业务层实现
     */
    @ApiOperation("导出数据")
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        dictService.exportData(response);

    }
    /**
     * 导入数据
     * 参数：MultipartFile
     */
    @ApiOperation("导入数据")
    @PostMapping("/importData")
    public R importData(MultipartFile file) {
        dictService.importData(file);
        return R.ok();

    }

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{parentDictCode}/{value}")
    public String getName(
            @ApiParam(name = "parentDictCode", value = "上级编码", required = true)
            @PathVariable("parentDictCode") String parentDictCode,
            @ApiParam(name = "value", value = "值", required = true)
            @PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue(parentDictCode, value);
    }

    @ApiOperation(value = "获取数据字典名称[国标数据]")
    @GetMapping(value = "/getName/{value}")
    public String getName(
            @ApiParam(name = "value", value = "值", required = true)
            @PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue("", value);
    }

    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public R findByDictCode(
            @ApiParam(name = "dictCode", value = "节点编码", required = true)
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return R.ok().data("list", list);

    }


}
