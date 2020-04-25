package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

/**
 * @Program: Itcast_health
 * @ClassName: RoleDao
 * @Description: 用户角色dao
 * @Author: KyleSun
 **/
public interface RoleDao {

    // 根据用户ID查询对应的角色
    Set<Role> findByUserId(Integer userId);
}
