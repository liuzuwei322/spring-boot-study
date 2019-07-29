package com.baidu.springbootstudy.mapper;

import com.baidu.springbootstudy.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select id, username as userName, password, create_time as createTime, status from t_user where username = #{userName} ")
    User findByUserName (String userName);
}
