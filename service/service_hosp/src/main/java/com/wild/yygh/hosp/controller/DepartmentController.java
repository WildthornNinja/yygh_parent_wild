package com.wild.yygh.hosp.controller;

import com.wild.yygh.common.R;
import com.wild.yygh.hosp.service.DepartmentService;
import com.wild.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "科室接口")
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    /**
     * 根据医院编号，查询医院所有科室列表
     *
     * @param hoscode
     * @return
     */
    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("/getDeptList/{hoscode}")
    public R getDeptList(@PathVariable String hoscode) {
        List<DepartmentVo> deptVoList = departmentService.findDeptTree(hoscode);
        return R.ok().data("list", deptVoList);
    }
}
