package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Program: Itcast_health
 * @ClassName: SpringSecurityUserService
 * @Description: Spring Security相关: 实现认证和授权
 * @Author: KyleSun
 **/
@Component
public class SpringSecurityUserService implements UserDetailsService {

    /*
        为什么controller调用的 此service不配置在dubbo中,而是配置在后台 ?

        spring-security.xml中配置了login接口: login-processing-url="/login.do",
        此接口在访问时,需要username和password,
        而username 通过服务实现类SpringSecurityUserService从数据库中获取用户user获得
     */


    // 使用dubbo通过网络远程调用服务提供方获取数据库中的用户信息
    @Reference
    private UserService userService;


    /**
     * @description: //TODO 根据用户名查询数据库获取用户信息: 基本信息+角色+权限
     * @param: [username]
     * @return: org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据库获取用户信息: 基本信息+角色+权限
        User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }

        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 获取该用户的角色和权限
        if (roles != null) {
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getKeyword()));
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null) {
                    for (Permission permission : permissions) {
                        authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }
        // 返回此user的原因: 是UserDetails接口的实现类
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }


}
