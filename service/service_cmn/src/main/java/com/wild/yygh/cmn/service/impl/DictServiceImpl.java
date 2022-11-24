package com.wild.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wild.yygh.cmn.mapper.DictMapper;
import com.wild.yygh.cmn.service.DictService;
import com.wild.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;
}
