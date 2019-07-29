package com.baidu.springbootstudy.config;

import com.baidu.springbootstudy.entity.Permission;
import com.baidu.springbootstudy.entity.Role;
import com.baidu.springbootstudy.entity.User;
import com.baidu.springbootstudy.mapper.UserMapper;
import com.baidu.springbootstudy.mapper.UserPermissionMapper;
import com.baidu.springbootstudy.mapper.UserRoleMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    UserPermissionMapper userPermissionMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String userName = user.getUserName();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        List<Role> roleList = userRoleMapper.findByUserName(userName);
        Set<String> set = roleList.stream().map(Role::getName).collect(Collectors.toCollection(HashSet::new));
        simpleAuthorizationInfo.setRoles(set);

        // 获取用户权限集
        List<Permission> permissionList = userPermissionMapper.findByUserName(userName);
        Set<String> permissionSet = permissionList.stream().map(Permission::getName).collect(Collectors.toCollection(HashSet::new));
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        // 查询数据库
        User user = userMapper.findByUserName(userName);
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new IncorrectCredentialsException("密码错误");
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }
}
