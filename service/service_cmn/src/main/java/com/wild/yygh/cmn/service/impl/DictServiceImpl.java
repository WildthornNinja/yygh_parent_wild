package com.wild.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.deploy.net.HttpResponse;
import com.sun.deploy.net.URLEncoder;
import com.wild.yygh.cmn.listener.DictListener;
import com.wild.yygh.cmn.mapper.DictMapper;
import com.wild.yygh.cmn.service.DictService;
import com.wild.yygh.common.exception.YyghException;
import com.wild.yygh.model.cmn.Dict;
import com.wild.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private DictListener dictListener;

    /**
     * 根据数据id查询子数据列表
     * @param id
     * @return
     */
    @Override
    //redis k:v  k=dict::selectIndexList value=要缓存的数据字典列表
    @Cacheable(value = "dict", key = "'selectIndexList'+#id")
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
     * 导出数据
     * 从Response对象中获取数据，具体在业务层实现
     */
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            /**
             * 1.设置response响应对象的基本参数
             */
            //设置媒体类型
            response.setContentType("application/vnd.ms-excel");
            //设置字符集
            response.setCharacterEncoding("utf-8");
            //设置文件名
            //这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            //设置响应头
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

            /**
             * 2.查询所有的字典数据
             */
            List<Dict> dictList = baseMapper.selectList(null);

            /**
             * 3.【数据对象转型】将数据库查询结果集Dict对象 转型为 DictEeVo对象
             */
            List<DictEeVo> dictEeVoList = new ArrayList<>();
            for (Dict dict : dictList) {
                DictEeVo dictEeVo = new DictEeVo();
                //使用BeanUtils工具类复制对象
                BeanUtils.copyProperties(dict,dictEeVo);
                dictEeVoList.add(dictEeVo);
            }
            /**
             * 4.调用EasyExcel方法生成文件
             */
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("数据接口").doWrite(dictEeVoList);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 导入数据
     * @param file
     */
    @Override
    public void importData(MultipartFile file) {
        try {
            //1.获取输入流
            InputStream inputStream = file.getInputStream();
            //2.调用EasyExcel方法读取数据
            EasyExcel.read(inputStream,DictEeVo.class,dictListener).sheet().doRead();
        }catch (IOException e){
            e.printStackTrace();
            throw new YyghException(20001,"导入失败");
        }

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
