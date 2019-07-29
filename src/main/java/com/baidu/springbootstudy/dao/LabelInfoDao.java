package com.baidu.springbootstudy.dao;

import com.baidu.springbootstudy.entity.LabelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelInfoDao extends JpaRepository<LabelInfo, Long> {
    LabelInfo findByNid(Long nid);
}
