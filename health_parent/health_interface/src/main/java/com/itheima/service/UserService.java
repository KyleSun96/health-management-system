package com.itheima.service;

import com.itheima.pojo.User;

/**
 * @Program: Itcast_health
 * @InterfaceName: UserService
 * @Description: 用户服务接口
 * @Author: KyleSun
 **/
public interface UserService {

    // 根据用户名查询数据库获取用户信息: 基本信息+角色+权限
    User findByUsername(String username);
}
