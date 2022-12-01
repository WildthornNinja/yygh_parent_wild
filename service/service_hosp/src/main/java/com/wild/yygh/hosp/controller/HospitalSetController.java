package com.wild.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wild.yygh.common.R;
import com.wild.yygh.common.exception.YyghException;
import com.wild.yygh.hosp.service.HospitalSetService;
import com.wild.yygh.model.hosp.HospitalSet;
import com.wild.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //医院设置接口
@RequestMapping("/admin/hosp/hospitalSet")
@Api(tags = "首页模块")
//@CrossOrigin //解决跨域问题
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 实现模拟登陆接口
     */
    @ApiOperation("模拟登陆")
    @PostMapping("/login")
    public R login(){
        //返回数据格式:{"code":20000,"data":{"token":"admin-token"}}
        return R.ok().data("token","admin-token");
    }
    @ApiOperation("模拟获取并返回用户信息")
    @GetMapping("/info")
    public R getInfo(){
        /**
         * 返回数据格式
         * {"code":20000,"data":{"roles":["admin"],
         * "introduction":"I am a super administrator",
         * "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
         * "name":"Super Admin"}}
         */
        //定义map集合封装返回结果信息
        Map<String,Object> map = new HashMap<>();
        map.put("roles","WildThorn");
        map.put("introduction","Fight Procrastination");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Wild Thorn");
        return R.ok().data(map);
    }
    @ApiOperation("模拟退出登录")
    @PostMapping("/logout")
    public R logout(){
        return  R.ok();
    }

    /**
     * 查询所有医院设置
     */
    @ApiOperation(value = "医院设置列表")
    @GetMapping("/findAll")
    public R findAll() {
        //测试异常处理
        try {
            int i=1/0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new YyghException(20001,"自定义异常处理");
        }
        List<HospitalSet> hospitalSetList = hospitalSetService.list();
        return R.ok().data("hospitalSetList",hospitalSetList);
    }
    /**
     * 删除医院设置
     */
    @ApiOperation("通过id删除医院信息")
    @DeleteMapping("/delete/{id}")
    public R deleteHospSetById(@PathVariable Long id){
        boolean result = hospitalSetService.removeById(id);
        if(result){
            return R.ok();
        }else{
            return R.error();
        }
    }
    /**
     * 新增医院设置
     */
    @ApiOperation("新增医院设置")
    @PostMapping("/add")
    public R addHospSet(@ApiParam(name="hospitalSet",value = "医院设置对象",required = true)
            @RequestBody HospitalSet hospitalSet){
        //参数判空
        if(hospitalSet != null){
            boolean result = hospitalSetService.save(hospitalSet);
            if(result){
                return R.ok();
            }else{
                return R.error();
            }
        }else {
            return R.error();
        }
    }
    /**
     * 修改医院设置
     */
    //1.数据回显
    @ApiOperation("通过ID查询医院设置")
    @GetMapping("/getById/{id}")
    public R getHospById(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return R.ok().data("hospitalSet",hospitalSet);
    }
    //2.进行更新操作
    @ApiOperation("修改医院设置")
    @PutMapping("/update")
    public R updateHospSet(@RequestBody HospitalSet hospitalSet){
        //参数判空
        if(hospitalSet != null){
            boolean result = hospitalSetService.updateById(hospitalSet);
            if(result){
                return R.ok();
            }else{
                return R.error();
            }
        }else {
            return R.error();
        }
    }
    /**
     * 批量删除
     */
    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("/bachRemove")
    public R bachRemove(@RequestBody List<Long> ids){
        //参数判空
        if(ids!=null){
            boolean result = hospitalSetService.removeByIds(ids);
            if(result){
                return R.ok();
            }else{
                return R.error();
            }
        }else {
            return R.error();
        }
    }
    /**
     * 医院设置锁定和解锁
     */
    @ApiOperation("医院设置锁定和解锁状态")
    @PutMapping("/lock/{id}/{status}")
    public R lockHospSet(@PathVariable Long id,@PathVariable Integer status){
        //考虑乐观锁状态，先查后改
        //1.根据id查询医院信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //2.设置状态
        hospitalSet.setStatus(status);
        //3.更新操作
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }
//    /**
//     * 分页查询
//     */
//    @ApiOperation("分页查询")
//    @GetMapping("/{page}/{limit}")
//    public R pageFind(@ApiParam(name = "page",value = "当前页码",required = true)
//                      @PathVariable Long page,
//                      @ApiParam(name = "limit",value = "每条记录数",required = true)
//                      @PathVariable Long limit){
//        //1.设置分页条件
//        Page<HospitalSet> pageParam = new Page<>(page,limit);
//        //2.执行查询操作
//        hospitalSetService.page(pageParam);
//        //返回分页结果集
//        List<HospitalSet> records = pageParam.getRecords();
//        long total = pageParam.getTotal();
//        return R.ok().data("records",records).data("total",total);
//    }
    /**
     * 分页-带条件 查询
     */
    @ApiOperation("分页-带条件 查询")
    @PostMapping("/{page}/{limit}")
    public R pageQuery(@ApiParam(name = "page",value = "当前页码",required = true)
                      @PathVariable Long page,
                       @ApiParam(name = "limit",value = "每条记录数",required = true)
                      @PathVariable Long limit,
                       @ApiParam(name = "hospitalSetQueryVo",value= "查询条件[查询对象]",required = false)
                       @RequestBody HospitalSetQueryVo hospitalSetQueryVo){
        //1.设置分页条件
        Page<HospitalSet> pageParam = new Page<>(page,limit);
        //2.准备查询条件
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        //参数判空
        if(hospitalSetQueryVo!=null){
            String hoscode = hospitalSetQueryVo.getHoscode();
            String hosname = hospitalSetQueryVo.getHosname();
            if (!StringUtils.isEmpty(hosname)) {
                queryWrapper.like("hosname", hosname);
            }

            if (!StringUtils.isEmpty(hoscode) ) {
                queryWrapper.eq("hoscode", hoscode);
            }
            hospitalSetService.page(pageParam, queryWrapper);
        }else{
            hospitalSetService.page(pageParam,queryWrapper);
        }
        //返回分页结果集
        List<HospitalSet> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("records",records).data("total",total);
    }

}
