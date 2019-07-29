package com.baidu.springbootstudy.mapper;

import com.baidu.springbootstudy.entity.Permission;
import java.util.List;

public interface UserPermissionMapper {
    List<Permission> findByUserName(String userName);
}