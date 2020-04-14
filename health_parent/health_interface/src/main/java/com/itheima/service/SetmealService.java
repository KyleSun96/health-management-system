package com.itheima.service;

import com.itheima.pojo.Setmeal;

/**
 * @program: Itcast_health
 * @InterfaceName: SetmealService
 * @description: 体检套餐服务接口
 * @author: KyleSun
 **/

public interface SetmealService {

    // 新增套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);


}