package com.baidu.springbootstudy.mapper;

import com.baidu.springbootstudy.entity.LabelInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LabelInfoMapper {
    @Select("select nid, label_name as labelName from label_info where nid = #{nid} ")
    public LabelInfo findByNid(Long nid);
}
