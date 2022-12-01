package com.wild.yygh.hosp.controller;

import com.wild.yygh.common.R;
import com.wild.yygh.hosp.service.HospitalService;
import com.wild.yygh.model.hosp.Hospital;
import com.wild.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(tags = "医院接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "带条件带分页查询医院列表")
    @GetMapping("/{page}/{limit}")
    public R index(@PathVariable Integer page,
                   @PathVariable Integer limit,
                   HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectPage(page, limit, hospitalQueryVo);
        return R.ok().data("pageModel", pageModel);
    }

    @ApiOperation(value = "更新医院上线状态")
    @GetMapping("updateStatus/{id}/{status}")
    public R lock(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "status", value = "状态（0：未上线 1：已上线）", required = true)
            @PathVariable("status") Integer status) {
        hospitalService.updateStatus(id, status);
        return R.ok();
    }
}
