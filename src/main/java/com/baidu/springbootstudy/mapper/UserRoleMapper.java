package com.baidu.springbootstudy.mapper;

import com.baidu.springbootstudy.entity.Role;
import java.util.List;

public interface UserRoleMapper {
    List<Role> findByUserName(String userName);
}