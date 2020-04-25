package com.itheima.service;

import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Program: Itcast_health
 * @ClassName: SpringSecurityUserService
 * @Description: 从数据库查询用户信息 : 加盐加密密码
 * @Author: KyleSun
 **/
public class SpringSecurityUserService2 implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Map<String, User> map = new HashMap<>();

    /**
     * @description: //TODO 模拟从数据库中查询到的用户数据
     * @param: []
     * @return: void
     */
    public void initUserData() {
        // 使用 bcrypt 提供的方法对密码进行加密
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("admin"));

        User user2 = new User();
        user2.setUsername("xiaoming");
        user2.setPassword(passwordEncoder.encode("1234"));

        map.put(user1.getUsername(), user1);
        map.put(user2.getUsername(), user2);
    }


    /**
     * @description: //TODO 根据用于输入的用户名 查询数据库用户信息
     * @param: [username]
     * @return: org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        initUserData();

        System.out.println("inputUsername: " + username);
        /*
            根据用户名查询数据库获得用户信息(包含数据库中存储的密码信息)
            将用户信息返回给框架
            框架会进行密码比对(页面提交的密码和数据库中查询的密码进行比对)
         */
        User user = map.get(username);
        if (user == null) {
            // 数据库中查询不到: 用户名不存在
            return null;
        } else {
            // 数据库可以查询到: 用户名存在 --> 将用户信息返回给框架
            // 为当前登录用户授权,后期需要改为从数据库查询当前用户对应的权限
            List<GrantedAuthority> list = new ArrayList<>();
            // 授权
            list.add(new SimpleGrantedAuthority("permission_A"));
            list.add(new SimpleGrantedAuthority("permission_B"));

            if (username.equals("admin")) {
                // 授予角色
                list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                // 授权
                list.add(new SimpleGrantedAuthority("add"));
            }
            return new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        }
    }
}
