package com.wild.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wild.yygh.cmn.client.DictFeignClient;
import com.wild.yygh.enums.DictEnum;
import com.wild.yygh.hosp.repository.HospitalRepository;
import com.wild.yygh.hosp.service.HospitalService;
import com.wild.yygh.model.hosp.Department;
import com.wild.yygh.model.hosp.Hospital;
import com.wild.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DictFeignClient dictFeignClient;

    /**
     * 存储医院数据
     *
     * @param paramMap
     */
    @Override
    public void saveHospital(Map<String, Object> paramMap) {
        //1.参数对象转型   map => hospital
        String paramJsonString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(paramJsonString, Hospital.class);
        //2. 先查询后保存 使用hoscode 查询医院信息
        Hospital targeHospital = hospitalRepository.getByHoscode(hospital.getHoscode());
        if(targeHospital!=null){
            //3.若存在医院信息 则更新 ，
            hospital.setId(targeHospital.getId());
            hospital.setCreateTime(targeHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(targeHospital.getIsDeleted());
            hospital.setStatus(targeHospital.getStatus());
            hospitalRepository.save(hospital);
        }else{
            //4.若不存在则新增
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospital.setStatus(0);
            hospitalRepository.save(hospital);
        }
    }

    /**
     * [查询]获取医院信息
     * @param hoscode
     * @return
     */
    @Override
    public Hospital getHospByHoscode(String hoscode) {
        //通过hoscode字段 查询医院信息
        Hospital hospital = hospitalRepository.getByHoscode(hoscode);
        return hospital;
    }

    /**
     * 带条件带分页查询医院列表
     * @param page
     * @param limit
     * @param hospitalQueryVo
     * @return
     */
    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //1.创建分页对象
        //1.1创建排序对象
        Sort sort = Sort.by(Sort.Direction.DESC, "creatTime");
        //1.2创建分页对象
        Pageable pageable = PageRequest.of((page - 1), limit, sort);

        //2.创建查询条件模板
        //2.1创建模板构造器
        ExampleMatcher matcher = ExampleMatcher.matching()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                //改变默认大小写忽略方式：忽略大小写
                .withIgnoreCase(true);
        //2.2封装查询条件
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        //2.3创建模板
        Example<Hospital> example = Example.of(hospital, matcher);

        //3.查询数据
        Page<Hospital> hospitalPage = hospitalRepository.findAll(example, pageable);
        //4.跨模块翻译字段
        hospitalPage.getContent().stream().forEach(item -> {
            //进行远程调用,翻译字段
            this.packHospital(item);
        });


        return hospitalPage;
    }

    /**
     * 远程调用翻译字段
     *
     * @param item
     */
    private Hospital packHospital(Hospital item) {
        //翻译省市区 国标数据
        String provinceString = dictFeignClient.getName(item.getProvinceCode());
        //市信息
        String cityString = dictFeignClient.getName(item.getCityCode());
        //区信息
        String districtString = dictFeignClient.getName(item.getDistrictCode());
        //翻译医院等级 【自定义信息】
        String hostypeString = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(), item.getHostype());
        //封装数据
        item.getParam().put("hostypeString", hostypeString);
        item.getParam().put("fullAddress", provinceString + cityString + districtString + item.getAddress());

        return item;
    }


}
