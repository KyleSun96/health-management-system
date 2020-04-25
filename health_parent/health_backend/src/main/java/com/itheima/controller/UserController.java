package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Program: Itcast_health
 * @ClassName: UserController
 * @Description: 用户相关管理
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * @description: //TODO 获得当前登录用户的用户名
     * @param: []
     * @return: com.itheima.entity.Result
     */
    @RequestMapping("/getUsername.do")
    public Result getUsername() {
        // 当Spring security完成认证后，会将当前用户信息保存到框架提供的上下文对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);

        if (user != null) {
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}
