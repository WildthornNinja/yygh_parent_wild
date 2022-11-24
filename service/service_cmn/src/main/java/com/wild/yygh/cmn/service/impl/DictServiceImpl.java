package com.wild.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wild.yygh.cmn.mapper.DictMapper;
import com.wild.yygh.cmn.service.DictService;
import com.wild.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;

    /**
     * 根据数据id查询子数据列表
     * @param id
     * @return
     */
    @Override
    public List<Dict> findChildData(Long id) {
        //1.通过父id查询出 数据集合
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        //2.执行查询条件 ,baseMapper 由ServiceImpl<DictMapper,Dict>类中注入
        List<Dict> dictList = baseMapper.selectList(queryWrapper);
        //3.遍历集合判断每一个节点元素是否有子数据
        for (Dict dict : dictList) {
            boolean hashChild = isChildren(dict.getId());
            //将判断结果[boolean]设置为dict对象 hasChildren属性
            dict.setHasChildren(hashChild);
        }


        return dictList;
    }

    /**
     * 判断是该dict对象否有子数据
     * @param id
     * @return
     */
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        //通过parent_id 父节点 该节点的子节点个数
        queryWrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(queryWrapper);
        //若count>0 则表明该dict对象有子节点数据
        return count>0;
    }
}
