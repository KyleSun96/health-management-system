package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

/**
 * @Program: Itcast_health
 * @InterfaceName: PermissionDao
 * @Description: 用户权限dao
 * @Author: KyleSun
 **/
public interface PermissionDao {

    // 根据角色ID查询关联的权限
    Set<Permission> findByRoleId(Integer roleId);
}
