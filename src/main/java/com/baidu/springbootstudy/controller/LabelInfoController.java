package com.baidu.springbootstudy.controller;

import com.baidu.springbootstudy.dao.LabelInfoDao;
import com.baidu.springbootstudy.entity.LabelInfo;
import com.baidu.springbootstudy.mapper.LabelInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LabelInfoController {
    private static final Logger logger = LoggerFactory.getLogger(LabelInfoController.class);

    @Autowired
    LabelInfoDao labelInfoDao;

    @Autowired
    LabelInfoMapper labelInfoMapper;

    @RequestMapping(value = "/findLabelInfoByNid", method = RequestMethod.GET)
    public LabelInfo findLabelInfoByNid (Long nid) {
        logger.info(labelInfoDao.findByNid(nid).toString());
        logger.info("mybatis方式：");
        logger.info(labelInfoMapper.findByNid(nid).toString());
        return labelInfoDao.findByNid(nid);
    }
}
