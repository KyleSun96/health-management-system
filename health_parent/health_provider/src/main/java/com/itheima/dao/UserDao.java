package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * @Program: Itcast_health
 * @ClassName: UserDao
 * @Description:
 * @Author: KyleSun
 **/
public interface UserDao {

    // 根据用户名查询数据库获取用户基本信息
    User findByUsername(String username);
}
