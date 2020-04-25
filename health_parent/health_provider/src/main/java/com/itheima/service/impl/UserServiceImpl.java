package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @Program: Itcast_health
 * @ClassName: UserServiceImpl
 * @Description: 用户服务实现
 * @Author: KyleSun
 **/
@Transactional
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    /**
     * @description: //TODO 根据用户名查询数据库获取用户信息: 基本信息+角色+权限
     * @param: [username]
     * @return: com.itheima.pojo.User
     */
    @Override
    public User findByUsername(String username) {
        // 根据用户名查询数据库获取用户基本信息
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }

        Integer userId = user.getId();
        // 根据用户ID查询对应的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            // 根据角色ID查询关联的权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            // 让角色关联权限
            role.setPermissions(permissions);
        }
        // 让用户关联角色
        user.setRoles(roles);

        return user;
    }


}
