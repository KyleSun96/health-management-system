package com.itheima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Program: Itcast_health
 * @ClassName: HelloController
 * @Description: 注解方式权限控制
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/hello")
public class HelloController {

    /**
     * @description: //TODO 调用此方法要求当前用户必须具有add权限
     * @param: []
     * @return: java.lang.String
     */
    @PreAuthorize("hasAuthority('add')")
    @RequestMapping("/add.do")
    public String add() {
        System.out.println("add success !");
        return "add success ! ";
    }


    /**
     * @description: //TODO 调用此方法要求当前用户必须具有ROLE_ADMIN角色
     * @param: []
     * @return: java.lang.String
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/delete.do")
    public String delete() {
        System.out.println("delete success !");
        return "delete success ! ";
    }

}
